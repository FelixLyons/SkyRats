package com.SkyRats.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class GUICommand implements CommandExecution {
    //Opens GUI when command executes
    @Override
    public void execute() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("test"));
    }
}
