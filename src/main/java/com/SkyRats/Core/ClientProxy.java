package com.SkyRats.Core;

import com.SkyRats.Core.Commands.CommandKeyInput;
import com.SkyRats.Core.Commands.CommandRegisterAll;
import com.SkyRats.Core.Features.AlertMessagePopup;
import com.SkyRats.Core.Features.ChatManager;
import com.SkyRats.Core.Features.MineshaftTracker;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.Features.Mining.ShaftDetector;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy  {
    private AlertMessagePopup alertPopup = new AlertMessagePopup();
    private MineshaftTracker tracker = new MineshaftTracker();
    private CommandRegisterAll commands = new CommandRegisterAll(
            tracker
    );

    public void init() {
        //Register commands
        commands.execute();
        //User chat binds setup
        CommandKeyInput.getUserBinds();
        //Register all events
        MinecraftForge.EVENT_BUS.register(new GuiOpener());
        MinecraftForge.EVENT_BUS.register(new ChatManager(alertPopup));
        MinecraftForge.EVENT_BUS.register(alertPopup);
        MinecraftForge.EVENT_BUS.register(new PlayerLocationChecker());
        MinecraftForge.EVENT_BUS.register(new CommandKeyInput());
        MinecraftForge.EVENT_BUS.register(new ShaftDetector(tracker));
    }
}
