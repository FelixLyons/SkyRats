package com.SkyRats;

import com.SkyRats.Commands.BaseCommand;
import com.SkyRats.Commands.CommandRegisterAll;
import com.SkyRats.GUI.GuiOpener;
import com.SkyRats.GUI.HomeGUI;
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
    public static final String VERSION = "0.01";
    private KeyBinding chatKey, commandKey;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Register commands
        CommandRegisterAll.execute();
        //Gets user keybind for chat
        chatKey = Minecraft.getMinecraft().gameSettings.keyBindChat;
        commandKey = Minecraft.getMinecraft().gameSettings.keyBindCommand;
        MinecraftForge.EVENT_BUS.register(new GuiOpener());
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
