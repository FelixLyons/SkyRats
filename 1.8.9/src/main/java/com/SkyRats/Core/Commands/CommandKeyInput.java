package com.SkyRats.Core.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class CommandKeyInput {
    private static KeyBinding chatKey;
    private static KeyBinding commandKey;

    //Get user keybinds for chats
    public static void getUserBinds() {
        chatKey = Minecraft.getMinecraft().gameSettings.keyBindChat;
        commandKey = Minecraft.getMinecraft().gameSettings.keyBindCommand;
    }

    //Custom chat to detect commands
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(commandKey.isPressed()) {
            //Command chat (/)
            Minecraft.getMinecraft().displayGuiScreen(new BaseCommand("/"));
        }else if(chatKey.isPressed()) {
            //Normal chat (clear)
            Minecraft.getMinecraft().displayGuiScreen(new BaseCommand(""));
        }
    }
}
