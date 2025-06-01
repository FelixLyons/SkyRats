package com.SkyRats.GUI;

import com.SkyRats.Core.Features.FeatureSettings;
import com.SkyRats.Core.Features.SettingButtons;
import com.SkyRats.Core.Features.SettingsManager;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.List;

public class HomeGUI extends GuiScreen {

    private int selectedFeature = 0;
    private final String[] features = {"Home", "Alerts", "Mining", "Rift", "Others"};
    private final int panelWidth = 500;
    private final int panelHeight = 320;
    private final int leftPanelWidth = 150;
    private final int totalWidth = leftPanelWidth + 350;

    public HomeGUI() {
        FeatureSettings.run();
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        //Panel sizes
        int panelX = (width - totalWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        // Draw left panel background
        drawRect(panelX, panelY, panelX + leftPanelWidth, panelY + panelHeight, 0xCC111111);
        // Draw right panel background
        drawRect(panelX + leftPanelWidth, panelY, panelX + panelWidth, panelY + panelHeight, 0xAA333333);

        //Title and creator
        int startY = panelY;
        int yPos = startY + 10;
        drawString(fontRendererObj, "§lSkyRats", panelX + 10, yPos, 0xCCCCCC);
        drawString(fontRendererObj, "By Sunaio & A_Blender_", panelX + 10, yPos + 11, 0xCCCCCC);

        // Draw feature texts on left panel
        startY = panelY + 40;
        for (int i = 0; i < features.length; i++) {
            yPos = startY + i * 22;

            // Check if mouse is hovering over this feature text
            boolean isHover = mouseX >= panelX + 10 && mouseX <= panelX + leftPanelWidth - 10 && mouseY >= yPos && mouseY <= yPos + 12;

            // Highlight selected or hovered text
            if (i == selectedFeature) {
                // light blue and underlined if selected
                drawString(fontRendererObj, "§n"+ features[i], panelX + 10, yPos, 0xFF55FFFF);
            } else if (isHover) {
                // light blue on hover
                drawString(fontRendererObj, features[i], panelX + 10, yPos, 0xFF55FFFF);
            } else {
                // normal color (white)
                drawString(fontRendererObj, features[i], panelX + 10, yPos, 0xFFFFFFFF);
            }
        }

        // Draw right panel content based on selected feature
        drawRightPanelContent(panelX + leftPanelWidth + 10, panelY + 20, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    //Detects if user clicks on options on the left panel
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            // Check for toggle interaction
            String featureKey = features[selectedFeature];
            List<SettingButtons> toggles = SettingsManager.getFeatureToggles(featureKey);
            if (toggles != null) {
                for (SettingButtons toggle : toggles) {
                    if (toggle.isHovered(mouseX, mouseY)) {
                        toggle.toggle();
                        return;
                    }
                }
            }

            // Check for feature sidebar selection
            int panelX = (width - panelWidth) / 2;
            int panelY = (height - panelHeight) / 2;
            int startY = panelY + 40;
            for (int i = 0; i < features.length; i++) {
                int yPos = startY + i * 22;
                if (mouseX >= panelX + 10 && mouseX <= panelX + leftPanelWidth - 10
                        && mouseY >= yPos && mouseY <= yPos + 12) {
                    selectedFeature = i;
                    break;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    //Right panel of GUI where all settings for selected feature is displayed.
    private void drawRightPanelContent(int x, int y, int mouseX, int mouseY) {
        String title = "Settings";
        drawString(fontRendererObj, title, x, y, 0xFFFFFFFF);
        List<SettingButtons> toggles = SettingsManager.getFeatureToggles(features[selectedFeature]);

        if (toggles != null && !toggles.isEmpty()) {
            int offsetY = y + 20;
            for (SettingButtons toggle : toggles) {
                toggle.setX(x);
                toggle.setY(offsetY);
                toggle.draw(mc, mouseX, mouseY);
                offsetY += toggle.getHeight() + 5;
            }
        } else {
            drawString(fontRendererObj, "WIP", x, y + 20, 0xAAAAAA);
        }
    }

    //Save settings on gui close
    @Override
    public void onGuiClosed() {
        SettingsManager.save();
        super.onGuiClosed();
    }
}


