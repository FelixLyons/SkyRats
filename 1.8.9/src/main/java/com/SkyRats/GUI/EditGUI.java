package com.SkyRats.GUI;

import com.SkyRats.Core.GUI.*;
import com.SkyRats.GUI.HUD.HUDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Edit GUI screen
public class EditGUI extends GuiScreen {
    private final File SAVEFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/hud_config.json");

    // Draw the GUI screen
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Title
        String title = "SR HUD Editor";
        int titleWidth = mc.fontRendererObj.getStringWidth(title);
        int centerX = this.width / 2;
        int posY = 10;

        // Draw Title Text
        mc.fontRendererObj.drawStringWithShadow(title, centerX - (titleWidth / 2), posY, 0xFFFFFF);

        // Instructions
        String[] instrLines = new String[]{
                "Scroll Wheel to resize",
                "Right click to reset size to default",
                "Backspace disable hovered HUD"
        };

        posY += 12; // space below title

        // Draw each instruction centered under the title
        for(String line : instrLines) {
            int lineWidth = mc.fontRendererObj.getStringWidth(line);
            mc.fontRendererObj.drawStringWithShadow(line, centerX - (lineWidth / 2), posY, 0xAAAAAA);
            posY += 10;
        }

        // Renders in all HUDs
        List<MovableUIs> huds = HUDManager.getAllHUDs();
        for(MovableUIs hud : huds) {
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
        for(MovableUIs hud : huds) {
            hud.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    // Check for mouse release on HUD
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        List<MovableUIs> huds = HUDManager.getAllHUDs();
        // Pass click event to each HUD for drag
        for(MovableUIs hud : huds) {
            hud.mouseReleased(); // mouse release
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 14) { // Backspace key
            // Convert raw mouse position to GUI coordinates
            int mouseX = Mouse.getX() * this.width / Minecraft.getMinecraft().displayWidth;
            int mouseY = this.height - Mouse.getY() * this.height / Minecraft.getMinecraft().displayHeight - 1;

            // Loop through HUDs and disable the hovered one if backspace is pressed on it
            for(MovableUIs hud : HUDManager.getAllHUDs()) {
                if(hud.isHovered(mouseX, mouseY)) {
                    String label = hud.getFeatureLabel();
                    if(label != null) {
                        List<Settings> hudSettings = FeatureSettings.getFeatureSettings().get("HUD");
                        if(hudSettings != null) {
                            for(Settings setting : hudSettings) {
                                if(setting.getLabel().equalsIgnoreCase(label)) {
                                    setting.setValue(false); // disables the feature
                                    // Saving disabled feature
                                    File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/config.json");
                                    SettingsManager.save(FeatureSettings.getFeatureSettings(), configFile);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
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

        for(MovableUIs ui : HUDManager.getAllHUDs()) {
            String name = ui.getName();
            // Wrap HUDSettings in a list with a single element
            List<HUDSettings> list = new ArrayList<HUDSettings>();
            list.add(new HUDSettings(name, ui.getX(), ui.getY(), ui.getScale()));;
            hudSaveData.put(name, list);
        }

        SettingsManager.save(hudSaveData, SAVEFILE);
        super.onGuiClosed();
    }

    // Handles mouse wheel input (resize HUD)
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // Get scroll amount
        int scroll = org.lwjgl.input.Mouse.getEventDWheel();
        if(scroll != 0) {
            // Get actual mouse position in screen space
            int mouseX = org.lwjgl.input.Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - org.lwjgl.input.Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

            // Find the hovered HUD and adjust its size (scale)
            for(MovableUIs hud : HUDManager.getAllHUDs()) {
                if (hud.isHovered(mouseX, mouseY)) {
                    float currentScale = hud.getScale();
                    float scaleStep = 0.1F;
                    float newScale = scroll > 0 ? currentScale + scaleStep : currentScale - scaleStep;
                    hud.setScale(newScale);
                    break;
                }
            }
        }
    }
}
