package com.SkyRats.Commands;

import com.SkyRats.GUI.GuiOpener;
import com.SkyRats.GUI.HomeGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;


public class MenuCommand implements CommandExecution {
    @Override
    public void execute() {
        Minecraft mc = Minecraft.getMinecraft();

        mc.thePlayer.addChatMessage(new ChatComponentText("SkyRats"));
        System.out.println("[DEBUG] Queuing HomeGUI to open next tick");

        GuiOpener.queueGui(new HomeGUI());
    }
}
