package com.SkyRats;

import com.SkyRats.Core.Commands.BaseCommand;
import com.SkyRats.Core.Commands.CommandRegisterAll;
import com.SkyRats.Core.Features.ChatManager;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.Core.Features.AlertMessagePopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = SkyRats.MODID, version = SkyRats.VERSION)
public class SkyRats
{
    public static final String MODID = "skyrats";
    public static final String VERSION = "0.05";
    private KeyBinding chatKey, commandKey;
    private AlertMessagePopup alertPopup = new AlertMessagePopup();

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Register commands
        CommandRegisterAll.execute();

        //Gets user keybind for chat
        chatKey = Minecraft.getMinecraft().gameSettings.keyBindChat;
        commandKey = Minecraft.getMinecraft().gameSettings.keyBindCommand;

        //Register all events
        MinecraftForge.EVENT_BUS.register(new GuiOpener());
        MinecraftForge.EVENT_BUS.register(new ChatManager(alertPopup));
        MinecraftForge.EVENT_BUS.register(alertPopup);
		MinecraftForge.EVENT_BUS.register(this);
    }

    //Custom chat to detect commands
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(commandKey.isPressed()) {
            //Command chat (/)
            Minecraft.getMinecraft().displayGuiScreen(new BaseCommand("/"));
        }else if(chatKey.isPressed()) {
            //Normal chat (clear)
            Minecraft.getMinecraft().displayGuiScreen(new BaseCommand(" "));
        }
    }
}
