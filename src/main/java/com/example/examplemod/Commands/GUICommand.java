package com.example.examplemod.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;

//Opens GUI when command executes
public class GUICommand extends GuiChat {
    public GUICommand() {
        super();
    }

    public GUICommand(String defaultInputText) {
        super(defaultInputText);
    }

    //Intercept message from being sent.
    @Override
    public void sendChatMessage(String message) {
        String msg = message.startsWith("/") ? message : "/" + message;
        if(message.equalsIgnoreCase("/gui")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("test"));
            return;
        }
        super.sendChatMessage(message);
    }
}
