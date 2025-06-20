package com.skyrats.Backend.GUIs;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;

public class GuiOpener {
    private static Screen screenToOpen = null;

    public static void queueGui(Screen screen) {
        screenToOpen = screen;
    }

    // Open GUI screen once other UIs are closed
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (screenToOpen != null && client.currentScreen != screenToOpen) {
                client.setScreen(screenToOpen);
                screenToOpen = null;
            }
        });
    }
}
