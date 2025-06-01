package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.GUI.HomeGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;


public class MenuCommand implements CommandExecution {
    @Override
    public void execute() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("hi"));
        GuiOpener.queueGui(new HomeGUI());
    }
}
