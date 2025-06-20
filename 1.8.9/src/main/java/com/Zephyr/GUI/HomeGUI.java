package com.Zephyr.GUI;

import com.Zephyr.Core.GUI.*;
import com.Zephyr.Zephyr;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeGUI extends GuiScreen {

    // Constants defining panel dimensions and padding
    private static final int LEFT_PANEL_WIDTH = 150;
    private static final int RIGHT_PANEL_WIDTH = 430;
    private static final int PANEL_HEIGHT = 320;
    private static final int PANEL_WIDTH = LEFT_PANEL_WIDTH + RIGHT_PANEL_WIDTH;
    private static final int FEATURE_TEXT_SPACING = 15;  // vertical space between feature items in left panel
    private static final int FEATURE_TEXT_HEIGHT = 12;   // height of feature text in left panel
    private static final int LEFT_PANEL_PADDING_X = 10;  // horizontal padding for left panel content
    private static final int LEFT_PANEL_PADDING_Y = 40;  // vertical padding for left panel content
    private static final int RIGHT_PANEL_PADDING_X = 10; // horizontal padding for right panel content
    private static final int RIGHT_PANEL_PADDING_Y = 20; // vertical padding for right panel content

    // Files for saving/loading configuration and HUD positions
    private final File SAVEFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/Zephyr/1.8.9/config.json");
    private final File HUDFILE = new File(Minecraft.getMinecraft().mcDataDir, "config/Zephyr/1.8.9/hud_config.json");

    // Index of currently selected feature tab
    private int selectedFeatureIndex = 0;
    // Feature tab names displayed in left panel
    private final String[] features = {"Home", "Alerts", "Mining", "Rift", "Others"};

    private EditButton editButton;    // Button used in Home tab to open HUD editor
    private final EditGUI EDITOR;     // Reference to the HUD editor GUI

    // Constructor: takes an EditGUI instance and runs feature settings initialization
    public HomeGUI(EditGUI editor) {
        this.EDITOR = editor;
        FeatureSettings.run();
    }

    @Override
    public void initGui() {
        // Initialize the "Edit" button with a click listener that applies HUD positions and opens the editor GUI
        this.editButton = new EditButton("Edit", new Runnable() {
            @Override
            public void run() {
                HUDSettings.applyHUDPositions(HUDFILE);
                GuiOpener.queueGui(EDITOR);
            }
        });

        // Initialize all feature dropdowns for the GUI
        FeatureDropdown.initializeDropdowns();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Calculate top-left corner of the main panel to center it on screen
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;

        // Draw background rectangles for both panels and separator line
        drawPanelsBackground(panelX, panelY);
        // Draw feature list on left panel
        drawLeftPanelFeatures(panelX, panelY, mouseX, mouseY);
        // Draw feature-specific content on right panel
        drawRightPanelContent(panelX + LEFT_PANEL_WIDTH + RIGHT_PANEL_PADDING_X, panelY + RIGHT_PANEL_PADDING_Y, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // Draw background rectangles for left and right panels, and the separator line
    private void drawPanelsBackground(int panelX, int panelY) {
        drawRect(panelX, panelY, panelX + LEFT_PANEL_WIDTH,
                panelY + PANEL_HEIGHT, 0xCC111111); // Left panel bg
        drawRect(panelX + LEFT_PANEL_WIDTH, panelY,
                panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xCC111111); // Right panel bg
        drawRect(panelX + LEFT_PANEL_WIDTH, panelY,
                panelX + LEFT_PANEL_WIDTH + 1, panelY + PANEL_HEIGHT,
                0xFF3F76E4); // Blue separator line
    }

    // Draw the list of feature tabs in the left panel
    private void drawLeftPanelFeatures(int panelX, int panelY, int mouseX, int mouseY) {
        int yStart = panelY + LEFT_PANEL_PADDING_Y;

        for (int i = 0; i < features.length; i++) {
            int yPos = yStart + i * FEATURE_TEXT_SPACING;

            // Check if mouse is hovering over this feature text
            boolean isHover = mouseX >= panelX + LEFT_PANEL_PADDING_X
                    && mouseX <= panelX + LEFT_PANEL_WIDTH - LEFT_PANEL_PADDING_X
                    && mouseY >= yPos
                    && mouseY <= yPos + FEATURE_TEXT_HEIGHT;

            int color;
            String text = features[i];

            // Highlight currently selected feature with underline and blue color
            if (i == selectedFeatureIndex) {
                text = EnumChatFormatting.UNDERLINE + text;
                color = 0xFF3F76E4;
            } else if (isHover) {
                // Highlight hovered feature with blue color
                color = 0xFF3F76E4;
            } else {
                // Default white color
                color = 0xFFFFFFFF;
            }

            // Draw feature text label
            drawString(fontRendererObj, text, panelX + LEFT_PANEL_PADDING_X, yPos, color);
        }

        // Draw GUI title and creator info at the top of the left panel
        drawTitleAndCreator(panelX);
    }

    // Draw the title "SkyRats" and the authors below it
    private void drawTitleAndCreator(int panelX) {
        int yPos = (height - PANEL_HEIGHT) / 2 + 10;
        drawString(fontRendererObj,
                EnumChatFormatting.BOLD + "" + EnumChatFormatting.UNDERLINE + "Zephyr",
                panelX + LEFT_PANEL_PADDING_X,
                yPos,
                0xFF3F76E4);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.87F, 0.87F, 1.0F);
        fontRendererObj.drawString(
                EnumChatFormatting.BOLD + "By Sunaio & A_Blender_",
                (int) ((panelX + LEFT_PANEL_PADDING_X) / 0.87F),
                (int) ((yPos + 14) / 0.87F),
                0xFF3F76E4
        );
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Only handle left click here
        if (mouseButton != 0) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        // Check if edit button is hovered
        if ("Home".equals(features[selectedFeatureIndex]) && editButton.isHovered(mouseX, mouseY)) {
            editButton.onClick();
            return;
        }

        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;

        // Check if the click was on the left panel feature list
        if (handleLeftPanelClick(mouseX, mouseY, panelX, panelY)) return;
        // Check if the click was on the right panel content (toggles, dropdowns)
        if (handleRightPanelClick(mouseX, mouseY)) return;

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    // Handle clicks on the left panel feature list; change selected feature if clicked
    private boolean handleLeftPanelClick(int mouseX, int mouseY, int panelX, int panelY) {
        int startY = panelY + LEFT_PANEL_PADDING_Y;

        for (int i = 0; i < features.length; i++) {
            int yPos = startY + i * FEATURE_TEXT_SPACING;
            if (mouseX >= panelX + LEFT_PANEL_PADDING_X && mouseX <= panelX + LEFT_PANEL_WIDTH - LEFT_PANEL_PADDING_X
                    && mouseY >= yPos && mouseY <= yPos + FEATURE_TEXT_HEIGHT) {
                selectedFeatureIndex = i;
                return true;
            }
        }
        return false;
    }

    // Handle clicks on the right panel elements like dropdowns and toggles
    private boolean handleRightPanelClick(int mouseX, int mouseY) {
        String currentFeature = features[selectedFeatureIndex];
        Map<String, List<FeatureDropdown>> dropdownMap = FeatureDropdown.getDropdownsByFeature();
        List<FeatureDropdown> dropdowns = dropdownMap.get(currentFeature);
        if (dropdowns != null) {
            int panelX = (width - PANEL_WIDTH) / 2 + LEFT_PANEL_WIDTH + RIGHT_PANEL_PADDING_X;
            int offsetY = (height - PANEL_HEIGHT) / 2 + RIGHT_PANEL_PADDING_Y + 20;

            // Check each dropdown if it was clicked
            for (FeatureDropdown dropdown : dropdowns) {
                if (dropdown.mouseClicked(mouseX, mouseY, panelX, offsetY)) {
                    return true;
                }
                offsetY += dropdown.getHeight(fontRendererObj) + 10;
            }
        }

        // Check toggles if clicked and toggle them
        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(features[selectedFeatureIndex]);
        if (toggles != null) {
            for (Settings toggle : toggles) {
                if (toggle.isHovered(mouseX, mouseY)) {
                    toggle.toggle();
                    Minecraft.getMinecraft().thePlayer.playSound("gui.button.press", 0.8F, 1.0F);
                    return true;
                }
            }
        }
        return false;
    }

    // Draw content for the right panel based on the selected feature tab
    private void drawRightPanelContent(int x, int y, int mouseX, int mouseY) {
        String currentFeature = features[selectedFeatureIndex];

        // Draw the feature tab title at the top
        drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + ""
                + EnumChatFormatting.BOLD + currentFeature, x, y, 0xFF3F76E4);

        int offsetY = y + 20;

        // If "Home" feature selected, draw Home specific content
        if ("Home".equals(currentFeature)) {
            drawHomePanelContent(x, offsetY, mouseX, mouseY);
            return;
        }

        // Get toggles and dropdowns for the current feature
        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(currentFeature);
        Map<String, List<FeatureDropdown>> dropdownList = FeatureDropdown.getDropdownsByFeature();
        List<FeatureDropdown> dropdowns = dropdownList.get(currentFeature);

        // If no toggles available, show "WIP"
        if (toggles == null || toggles.isEmpty()) {
            drawString(fontRendererObj, "WIP", x, offsetY, 0xAAAAAA);
            return;
        }

        // Keep track of toggles already drawn inside dropdowns to avoid duplicates
        Set<Settings> drawnInDropdown = new HashSet<Settings>();
        if (dropdowns != null) {
            for (FeatureDropdown dropdown : dropdowns) {
                dropdown.draw(mc, fontRendererObj, x, offsetY, mouseX, mouseY);
                drawnInDropdown.addAll(dropdown.getToggles());
                offsetY += dropdown.getHeight(fontRendererObj) + 10;
            }
        }

        // Draw toggles not contained inside dropdowns individually
        if (toggles != null) {
            for (Settings toggle : toggles) {
                if (drawnInDropdown.contains(toggle)) continue;

                // Set toggle position further right for better alignment
                int toggleX = x + 20;
                toggle.setX(toggleX);
                toggle.setY(offsetY);

                // Toggle label color depends on toggle state
                int labelColor = toggle.getValue() ? 0xFF3F76E4 : 0xFF555555;
                fontRendererObj.drawString(toggle.getLabel(), x, offsetY + 4, labelColor);
                toggle.draw(mc, mouseX, mouseY);

                offsetY += toggle.getHeight();

                // Draw optional description in smaller scaled font below toggle label
                String desc = toggle.getDescription();
                if (desc != null && !desc.isEmpty()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.77F, 0.77F, 1.0F);
                    int descColor = toggle.getValue() ? 0xFFFFFFFF : 0xFF555555;
                    drawString(fontRendererObj,
                            desc,
                            (int) ((x + 4) / 0.77F),
                            (int) ((offsetY + 2) / 0.77F),
                            descColor);
                    GlStateManager.popMatrix();
                    offsetY += 14;
                } else {
                    offsetY += 5;
                }
            }
        }
    }

    // Draw content specific to the "Home" tab
    private void drawHomePanelContent(int x, int offsetY, int mouseX, int mouseY) {
        // Display version string
        fontRendererObj.drawString("Zephyr v" + Zephyr.VERSION, x, offsetY, 0xFF3F76E4);
        offsetY += 18;

        int rowHeight = 14;
        int rowY = offsetY;
        int textY = rowY + (rowHeight - fontRendererObj.FONT_HEIGHT) / 2;
        fontRendererObj.drawString("Edit HUD Location", x, textY, 0xFF3F76E4);

        // Position and draw the "Edit" button for HUD editing
        editButton.setPosition(x + 350, rowY);
        editButton.draw(mc, mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        // Save feature settings to config file on GUI close
        SettingsManager.save(FeatureSettings.getFeatureSettings(), SAVEFILE);
        super.onGuiClosed();
    }
}