package com.Zephyr.Core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureDropdown {
    private final String label; // Dropdown label (e.g. "Mineshaft")
    private boolean expanded; // Whether dropdown is expanded or collapsed
    private final List<Settings> toggles; // Toggles shown under this dropdown
    private static final Map<String, List<FeatureDropdown>> dropdownsByFeature = new HashMap<String, List<FeatureDropdown>>(); // Map of dropdowns per feature

    // Animation state tracking
    private long animationStartTime = -1;
    private boolean animating = false;

    public FeatureDropdown(String label, List<Settings> toggles) {
        this.label = label;
        this.toggles = toggles;
        this.expanded = false;
    }

    // Draw the dropdown and its toggles (with animation)
    public void draw(Minecraft mc, FontRenderer font, int x, int y, int mouseX, int mouseY) {
        int dropdownWidth = 400;
        int headerHeight = 16;
        String arrow = expanded ? "\u25BC" : "\u25BA"; // ▼ or ►

        // Handle animation progress
        float animationProgress = 0f;
        if (animating) {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            int animationDuration = 300;
            float progress = Math.min(1f, (float) elapsed / animationDuration);
            animationProgress = expanded ? progress : 1f - progress;
            if (progress >= 1f) animating = false;
        } else {
            animationProgress = expanded ? 1f : 0f;
        }

        int fullHeight = getTotalToggleHeight(font);
        int togglesVisibleHeight = (int) (fullHeight * animationProgress);

        // Draw dropdown label text
        int labelColor = 0xFF3F76E4;
        int labelX = x + 6;
        int labelY = y + (headerHeight - 8) / 2;
        font.drawString(label, labelX, labelY, labelColor);

        // Draw the arrow icon scaled larger
        GlStateManager.pushMatrix();
        float scale = 1.4F;
        GlStateManager.scale(scale, scale, 1.0F);
        int arrowX = (int) ((x + dropdownWidth - font.getStringWidth(arrow) - 5) / scale);
        int arrowY = (int) (labelY / scale);
        font.drawString(arrow, arrowX, arrowY, labelColor);
        GlStateManager.popMatrix();

        // Skip rendering toggles if fully collapsed and not animating
        if (animationProgress <= 0f && !animating) return;

        // Clip toggle area during animation
        GlStateManager.pushMatrix();
        enableScissorBox(x, y + headerHeight, dropdownWidth, togglesVisibleHeight);

        int offsetY = y + headerHeight;

        // Draw each toggle inside the dropdown
        for (Settings toggle : toggles) {
            int toggleHeight = toggle.getHeight();
            String desc = toggle.getDescription();
            boolean hasDesc = desc != null && !desc.isEmpty();

            toggle.setX(x + 20);
            toggle.setY(offsetY);

            int toggleLabelColor = toggle.getValue() ? 0xFF3F76E4 : 0xFF555555;
            font.drawString(toggle.getLabel(), x + 20, offsetY + 4, toggleLabelColor);

            offsetY += toggleHeight;

            // Draw toggle description text below, if available
            if (hasDesc) {
                offsetY += 2;
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.77F, 0.77F, 1.0F);
                int descColor = toggle.getValue() ? 0xFFFFFFFF : 0xFF555555;
                font.drawString(
                        desc,
                        (int) ((x + 24) / 0.77F),
                        (int) ((offsetY) / 0.77F),
                        descColor
                );
                GlStateManager.popMatrix();
                offsetY += 12;
            }

            toggle.draw(mc, mouseX, mouseY);
            offsetY += 3;
        }

        // Disable clipping
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    // Get full height of dropdown including toggles (when fully expanded)
    public int getHeight(FontRenderer font) {
        int height = 16; // header
        if (expanded) {
            for (Settings s : toggles) {
                height += s.getHeight() + 3;
                if (s.getDescription() != null && !s.getDescription().isEmpty()) {
                    height += 14;
                }
            }
        }
        return height;
    }

    // Get total height needed for toggles for animation calculations
    private int getTotalToggleHeight(FontRenderer font) {
        int height = 0;
        for (Settings toggle : toggles) {
            height += toggle.getHeight() + 6;
            if (toggle.getDescription() != null && !toggle.getDescription().isEmpty()) {
                height += 14;
            }
        }
        return height;
    }

    // Return all toggles associated with this dropdown
    public List<Settings> getToggles() {
        return toggles;
    }

    // Handle mouse clicks on header and toggles
    public boolean mouseClicked(int mouseX, int mouseY, int x, int y) {
        int headerHeight = 16;
        int dropdownWidth = 400;

        // Click on header toggles expanded/collapsed state
        if (mouseX >= x && mouseX <= x + dropdownWidth && mouseY >= y && mouseY <= y + headerHeight) {
            expanded = !expanded;
            animating = true;
            animationStartTime = System.currentTimeMillis();
            return true;
        }

        if (!expanded) return false;

        // Check toggle click interactions
        int offsetY = y + 16;
        for (Settings toggle : toggles) {
            toggle.setX(x + 20);
            toggle.setY(offsetY);
            if (toggle.isHovered(mouseX, mouseY)) {
                toggle.toggle();

                // Handle synchronization logic between HUD and tracker
                if (toggle.getLabel().equalsIgnoreCase("Mineshaft Tracker HUD") || toggle.getLabel().equalsIgnoreCase("Mineshaft Tracker")) {
                    syncToggle("Mineshaft Tracker HUD", "Mining", "Mineshaft Tracker", toggle);
                } else if (toggle.getLabel().equalsIgnoreCase("Split or Steal HUD") || toggle.getLabel().equalsIgnoreCase("Split or Steal Tracker")) {
                    syncToggle("Split or Steal HUD", "Rift", "Split or Steal Tracker", toggle);
                }

                Minecraft.getMinecraft().thePlayer.playSound("gui.button.press", 0.8F, 1.0F);
                return true;
            }

            offsetY += toggle.getHeight() + 5;
            if (toggle.getDescription() != null && !toggle.getDescription().isEmpty()) {
                offsetY += 14;
            }
        }

        return false;
    }

    // Synchronize linked HUD and tracker toggles based on logic
    private void syncToggle(String hudLabel, String trackerCategory, String trackerLabel, Settings toggledSetting) {
        boolean isHUD = toggledSetting.getLabel().equalsIgnoreCase(hudLabel);
        boolean isTracker = toggledSetting.getLabel().equalsIgnoreCase(trackerLabel);

        boolean hudWasOn = FeatureSettings.isFeatureEnabled("HUD", hudLabel);
        boolean trackerWasOn = FeatureSettings.isFeatureEnabled(trackerCategory, trackerLabel);

        if (isHUD && toggledSetting.getValue()) {
            setToggle(trackerCategory, trackerLabel, true);
        } else if (isTracker && toggledSetting.getValue() && !trackerWasOn) {
            if (!hudWasOn) {
                setToggle("HUD", hudLabel, true);
            }
        } else if (isTracker && !toggledSetting.getValue()) {
            if (hudWasOn) {
                setToggle("HUD", hudLabel, false);
            }
        }
    }

    // Update toggle setting in another category
    private void setToggle(String category, String label, boolean value) {
        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(category);
        if (toggles != null) {
            for (Settings setting : toggles) {
                if (setting.getLabel().equalsIgnoreCase(label)) {
                    setting.setValue(value);
                    System.out.println("[SyncToggle] Set " + label + " in " + category + " to " + value);
                    return;
                }
            }
        }
    }

    // Configure OpenGL scissor box to limit rendering area (for animation clipping)
    private void enableScissorBox(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int displayHeight = mc.displayHeight;
        int displayWidth = mc.displayWidth;

        int scaleFactor = 1;
        int guiScale = mc.gameSettings.guiScale;
        if (guiScale == 0) guiScale = 1000;
        while (scaleFactor < guiScale &&
                displayWidth / (scaleFactor + 1) >= 320 &&
                displayHeight / (scaleFactor + 1) >= 240) {
            scaleFactor++;
        }

        int scissorX = x * scaleFactor;
        int scissorY = displayHeight - (y + height) * scaleFactor;
        int scissorW = width * scaleFactor;
        int scissorH = height * scaleFactor;

        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor(scissorX, scissorY, scissorW, scissorH);
    }

    // Initialize dropdowns and associate multiple toggles with each
    public static void initializeDropdowns() {
        // Prevent duplicate dropdowns from accumulating
        dropdownsByFeature.clear();
        // Define dropdown groups:
        // Format: { category, dropdown display name, toggle label 1, toggle label 2, ... }
        String[][] dropdownDefs = {
                {"Mining", "Mineshafts", "Mineshaft Tracker", "Mineshaft Tracker HUD"},
                {"Rift", "Split or Steal", "Split or Steal Tracker", "Split or Steal HUD"}
        };

        // Loop through each dropdown definition
        for (int i = 0; i < dropdownDefs.length; i++) {
            String[] def = dropdownDefs[i];

            // Skip invalid definitions (must have at least 3 elements)
            if (def.length < 3) {
                // Must have at least category, dropdown name, and one toggle
                continue;
            }

            // Extract category and display name for the dropdown
            String category = def[0];
            String dropdownName = def[1];

            // List to store matched toggle Settings objects
            List<Settings> combinedToggles = new ArrayList<Settings>();

            // Search each toggle label (starting from index 2) in both the specified category and "HUD"
            for (int j = 2; j < def.length; j++) {
                String label = def[j];

                // Try to find the label in the main feature category
                List<Settings> categorySettings = FeatureSettings.getFeatureSettings().get(category);
                if (categorySettings != null) {
                    for (Settings s : categorySettings) {
                        if (label.equalsIgnoreCase(s.getLabel())) {
                            combinedToggles.add(s); // Add matching toggle
                            break;
                        }
                    }
                }

                // Try to find the label in the "HUD" category
                List<Settings> hudSettings = FeatureSettings.getFeatureSettings().get("HUD");
                if (hudSettings != null) {
                    for (Settings s : hudSettings) {
                        if (label.equalsIgnoreCase(s.getLabel())) {
                            combinedToggles.add(s); // Add matching HUD toggle
                            break;
                        }
                    }
                }
            }

            // Get or create the list of dropdowns for the current category
            List<FeatureDropdown> dropdownList = dropdownsByFeature.get(category);
            if (dropdownList == null) {
                dropdownList = new ArrayList<FeatureDropdown>();
                dropdownsByFeature.put(category, dropdownList);
            }

            // Add a new FeatureDropdown to the list with the collected toggles
            dropdownList.add(new FeatureDropdown(dropdownName, combinedToggles));
        }
    }

    // Return all dropdowns grouped by feature category
    public static Map<String, List<FeatureDropdown>> getDropdownsByFeature() {
        return dropdownsByFeature;
    }
}