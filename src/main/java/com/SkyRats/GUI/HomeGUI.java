package com.SkyRats.GUI;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class HomeGUI extends GuiScreen {

    private int selectedFeature = 0;
    private final String[] features = {"Home", "Alerts", "Mining", "Rift", "Others"};
    private final int panelWidth = 500;
    private final int panelHeight = 320;
    private final int leftPanelWidth = 200;

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        // Draw left panel background
        drawRect(panelX, panelY, panelX + leftPanelWidth, panelY + panelHeight, 0xCC111111);
        // Draw right panel background
        drawRect(panelX + leftPanelWidth, panelY, panelX + panelWidth, panelY + panelHeight, 0xAA333333);

        int startY = panelY;
        int yPos = startY + 10;
        drawString(fontRendererObj, "§lSkyRats", panelX + 10, yPos, 0xCCCCCC);
        drawString(fontRendererObj, "By Sunaio & a_Blender_", panelX + 10, yPos + 11, 0xCCCCCC);

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
        drawRightPanelContent(panelX + leftPanelWidth + 10, panelY + 20);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {  // left click
            int panelX = (width - panelWidth) / 2;
            int panelY = (height - panelHeight) / 2;
            int startY = panelY + 40;

            for (int i = 0; i < features.length; i++) {
                int yPos = startY + i * 22;

                // Check if click is inside this feature text area
                if (mouseX >= panelX + 10 && mouseX <= panelX + leftPanelWidth - 10
                        && mouseY >= yPos && mouseY <= yPos + 12) {
                    selectedFeature = i;
                    break;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void drawRightPanelContent(int x, int y) {
        String title = "Settings";
        drawString(fontRendererObj, title, x, y, 0xFFFFFFFF);

        switch (selectedFeature) {
            case 0: // Home
                drawString(fontRendererObj, " Home WIP", x, y + 20, 0xAAAAAA);
                break;
            case 1: // Alerts
                drawString(fontRendererObj, " Alerts WIP", x, y + 20, 0xAAAAAA);
                break;
            case 2: // Mining
                drawString(fontRendererObj, "Mining WIP", x, y + 20, 0xAAAAAA);
                break;
            case 3: // Rift
                drawString(fontRendererObj, "Rift WIP", x, y + 20, 0xAAAAAA);
                break;
            case 4: // Others
                drawString(fontRendererObj, "Others WIP", x, y + 20, 0xAAAAAA);
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}


