package com.SkyRats.GUI;

import com.SkyRats.Core.Features.ColorAnimations;
import com.SkyRats.Core.GUI.*;
import com.SkyRats.SkyRats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HomeGUI extends GuiScreen {

    private final File SAVEFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/config.json");
    private final File HUDFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/hud_config.json");
    private int selectedFeature = 0;
    private final String[] features = {"Home", "HUD", "Alerts", "Mining", "Rift", "Others"};
    private final int leftPanelWidth = 150;
    private final int rightPanelWidth = 430;
    private final int panelWidth = leftPanelWidth + rightPanelWidth;
    private final int panelHeight = 320;
    private EditButton editButton;

    public HomeGUI() {
        FeatureSettings.run();
    }

    @Override
    public void initGui() {
        this.editButton = new EditButton("Edit", new Runnable() {
            @Override
            public void run() {
                HUDSettings.applyHUDPositions(HUDFILE);
                GuiOpener.queueGui(new EditGUI());
            }
        });
    }

    // Draws /sr menu
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        /* Chroma Color
        int chroma = ColorAnimations.getChromaColor(8f, 0f);
        */

        // Panel sizes
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        // Draw left panel background
        drawRect(panelX, panelY, panelX + leftPanelWidth, panelY + panelHeight, 0xCC111111);
        // Draw right panel background
        drawRect(panelX + leftPanelWidth, panelY, panelX + panelWidth, panelY + panelHeight, 0xCC111111);

        // Title and creator
        int startY = panelY;
        int yPos = startY + 10;
        drawString(fontRendererObj,
                EnumChatFormatting.BOLD + "" + EnumChatFormatting.UNDERLINE + "SkyRats",
                panelX + 10,
                yPos,
                0xFF3F76E4);
        // Making text smaller for creator
        GlStateManager.pushMatrix();
        // Scaled down by 10%
        GlStateManager.scale(0.87F, 0.87F, 1.0F);
        fontRendererObj.drawString(
                EnumChatFormatting.BOLD + "By Sunaio & A_Blender_",
                (int) ((panelX + 10) / 0.87F),
                (int) ((yPos + 14) / 0.87F),
                0xFF3F76E4
        );

        GlStateManager.popMatrix();

        // Draw vertical separator line between panels
        int lineX = panelX + leftPanelWidth;
        drawRect(lineX, panelY, lineX + 1, panelY + panelHeight, 0xFF3F76E4);

        // Draw feature texts on left panel
        startY = panelY + 40;
        for (int i = 0; i < features.length; i++) {
            yPos = startY + i * 15; // spacing

            // Check if mouse is hovering over this feature text
            boolean isHover = mouseX >= panelX + 10
                    && mouseX <= panelX + leftPanelWidth - 10
                    && mouseY >= yPos
                    && mouseY <= yPos + 12;

            // Highlight selected or hovered text
            if (i == selectedFeature) {
                // light blue and underlined if selected
                drawString(fontRendererObj,
                        EnumChatFormatting.UNDERLINE + features[i],
                        panelX + 10, yPos,
                        0xFF3F76E4);
            } else if (isHover) {
                // light blue on hover
                drawString(fontRendererObj,features[i], panelX + 10, yPos, 0xFF3F76E4);
            } else {
                // normal color (white)
                drawString(fontRendererObj, features[i], panelX + 10, yPos, 0xFFFFFF);
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
            String featureKey = features[selectedFeature];
            // Click for Edit Button
            if (featureKey.equals("Home") && editButton != null && editButton.isHovered(mouseX, mouseY)) {
                editButton.onClick();
                return;
            }

            // Check for toggle interaction
            List<Settings> toggles = FeatureSettings.getFeatureSettings().get(featureKey);
            if (toggles != null) {
                for (Settings toggle : toggles) {
                    if (toggle.isHovered(mouseX, mouseY)) {
                        toggle.toggle();
                        Minecraft.getMinecraft().thePlayer.playSound("gui.button.press", 0.8F, 1.0F);
                        return;
                    }
                }
            }

            // Check for feature sidebar selection
            int panelX = (width - panelWidth) / 2;
            int panelY = (height - panelHeight) / 2;
            int startY = panelY + 40;
            for (int i = 0; i < features.length; i++) {
                int yPos = startY + i * 15; // spacing (change this if you changed spacing for left panel)
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
        //int chroma = ColorAnimations.getChromaColor(8f, 0f);
        drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + features[selectedFeature],
                x,
                y,
                0xFF3F76E4);

        int offsetY = y + 20; // spacing

        // Edit GUI Location button
        if (features[selectedFeature].equals("Home")) {
            // Draw version text
            fontRendererObj.drawString(
                    "SkyRats v" + SkyRats.VERSION,
                    x,
                    offsetY,
                    0xFF3F76E4
            );

            offsetY += 18; // spacing

            int rowHeight = 14;
            int rowY = offsetY;

            // Center the label vertically within the row
            int textY = rowY + (rowHeight - fontRendererObj.FONT_HEIGHT) / 2;
            fontRendererObj.drawString("Edit HUD Location", x, textY, 0xFF3F76E4);

            // Align button on same row
            editButton.setPosition(x + 350, rowY);
            editButton.draw(mc, mouseX, mouseY);

            return;
        }

        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(features[selectedFeature]);
        if ((toggles == null || toggles.isEmpty()) && !features[selectedFeature].equals("Home")) {
            drawString(fontRendererObj, "WIP", x, offsetY, 0xAAAAAA);
            return;
        }

        if(toggles != null) {
            for (Settings toggle : toggles) {
                toggle.setX(x);
                toggle.setY(offsetY);

                // Draw label with blue if ON, dark gray if OFF
                int labelColor = toggle.getValue() ? 0xFF3F76E4 : 0xFF555555; // blue if on or gray if off
                fontRendererObj.drawString(toggle.getLabel(), x, offsetY + 4, labelColor);
                toggle.draw(mc, mouseX, mouseY);

                String desc = toggle.getDescription();
                offsetY += toggle.getHeight();

                if (desc != null && !desc.isEmpty()) {
                    // Scaling down text size
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.77F, 0.77F, 1.0F);
                    int descColor = toggle.getValue() ? 0xFFFFFFFF : 0xFF555555;  // white if ON, gray if OFF
                    drawString(fontRendererObj,
                            desc,
                            (int) ((x + 4) / 0.77F),
                            (int) ((offsetY + 2) / 0.77F),
                            descColor
                    );
                    GlStateManager.popMatrix();
                    offsetY += 14;
                } else {
                    offsetY += 5;
                }
            }
        }
    }

    //Save settings on gui close
    @Override
    public void onGuiClosed() {
        SettingsManager.save(FeatureSettings.getFeatureSettings(), SAVEFILE);
        super.onGuiClosed();
    }
}


