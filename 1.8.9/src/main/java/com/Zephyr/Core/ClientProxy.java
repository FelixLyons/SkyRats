package com.Zephyr.Core;

import com.Zephyr.Core.Commands.CommandKeyInput;
import com.Zephyr.Core.Commands.CommandRegisterAll;
import com.Zephyr.Core.Features.Notifications.AlertMessagePopup;
import com.Zephyr.Core.GUI.FeatureSettings;
import com.Zephyr.Core.Features.TimerTracker;
import com.Zephyr.Core.GUI.HUDSettings;
import com.Zephyr.Features.Alerts.ChatManager;
import com.Zephyr.Core.Features.Mineshafts.MineshaftTracker;
import com.Zephyr.Core.Features.PlayerLocationChecker;
import com.Zephyr.Core.GUI.GuiOpener;
import com.Zephyr.Features.Mining.ShaftDetector;
import com.Zephyr.Features.Rift.StealOrSplitTracker;
import com.Zephyr.GUI.EditGUI;
import com.Zephyr.GUI.HUD.HUDManager;
import com.Zephyr.GUI.HUD.HUDRenderer;
import com.Zephyr.GUI.HUD.ShaftDisplay;
import com.Zephyr.GUI.HUD.SplitOrStealDisplay;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy  {
    private AlertMessagePopup alertPopup = new AlertMessagePopup();
    private MineshaftTracker tracker = new MineshaftTracker();
    private EditGUI editor = new EditGUI();
    private CommandRegisterAll commands = new CommandRegisterAll(
            tracker,
            editor
    );

    // HUDs Objects
    private ShaftDisplay shaftDisplay = new ShaftDisplay(tracker);
    private SplitOrStealDisplay sosDisplay = new SplitOrStealDisplay();

    public void init() {
        // Register HUDs
        HUDManager.register(shaftDisplay);
        HUDManager.register(sosDisplay);

        // Load and initialize settings
        FeatureSettings.run();
        TimerTracker.load();
        HUDSettings.load();

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
        MinecraftForge.EVENT_BUS.register(new StealOrSplitTracker());

        // HUD Renderer
        MinecraftForge.EVENT_BUS.register(new HUDRenderer(
                shaftDisplay,
                sosDisplay));
    }
}
