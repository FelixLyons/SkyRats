package com.SkyRats.Core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiOpener {
    private static GuiScreen guiToOpen = null;

    public static void queueGui(GuiScreen gui) {
        guiToOpen = gui;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }
    }
}
