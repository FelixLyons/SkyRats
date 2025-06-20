package com.Zephyr.Core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// Helper class to open GUI screens
public class GuiOpener {
    private static GuiScreen guiToOpen = null;

    public static void queueGui(GuiScreen gui) {
        guiToOpen = gui;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Only execute once per tick at the END phase
        if(event.phase == TickEvent.Phase.END && guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
            // Clear the queue to avoid reopening
            guiToOpen = null;
        }
    }
}
