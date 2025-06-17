package com.SkyRats.GUI;

import com.SkyRats.Core.GUI.*;
import com.SkyRats.GUI.HUD.HUDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Edit GUI screen
public class EditGUI extends GuiScreen {
    private final File SAVEFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/Mineshaft/config.json");

    // Draw the GUI screen
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Renders in all HUDs
        List<MovableUIs> huds = HUDManager.getAllHUDs();
        for (MovableUIs hud : huds) {
            // Allow drag handling
            hud.handleDrag(mouseX, mouseY);

            // Render dummy content
            hud.renderDummy(mc);
        }

        // Draw screen
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // Check for mouse clicked on HUD (dragging)
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        List<MovableUIs> huds = HUDManager.getAllHUDs();
        for (MovableUIs hud : huds) {
            hud.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    // Check for mouse release on HUD
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        List<MovableUIs> huds = HUDManager.getAllHUDs();
        for (MovableUIs hud : huds) {
            hud.mouseReleased();
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    // Doesn't pause the game
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // Save position when GUI closes
    @Override
    public void onGuiClosed() {
        Map<String, List<HUDSettings>> hudSaveData = new HashMap<String, List<HUDSettings>>();

        for (MovableUIs ui : HUDManager.getAllHUDs()) {
            String name = ui.getName();
            // Wrap HUDSettings in a list with a single element
            List<HUDSettings> list = new ArrayList<HUDSettings>();
            list.add(new HUDSettings(name, ui.getX(), ui.getY()));
            hudSaveData.put(name, list);
        }

        SettingsManager.save(hudSaveData, SAVEFILE);
        super.onGuiClosed();
    }
}
