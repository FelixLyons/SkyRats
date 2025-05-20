package com.example.examplemod;

import com.example.examplemod.Commands.GUICommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    private KeyBinding chatKey, commandKey;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Gets user keybind for chat
        chatKey = Minecraft.getMinecraft().gameSettings.keyBindChat;
        commandKey = Minecraft.getMinecraft().gameSettings.keyBindCommand;
		MinecraftForge.EVENT_BUS.register(this);
    }

    //Custom chat to detect commands
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(chatKey.isPressed()) {
            //Normal chat (clear)
            Minecraft.getMinecraft().displayGuiScreen(new GUICommand(""));
        }else if(commandKey.isPressed()) {
            //Command chat (/)
            Minecraft.getMinecraft().displayGuiScreen(new GUICommand("/"));
        }
    }
}
