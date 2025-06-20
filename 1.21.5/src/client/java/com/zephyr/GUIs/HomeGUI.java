package com.zephyr.GUIs;

import com.zephyr.Backend.GUIs.Features;
import com.zephyr.Backend.GUIs.FeaturesToggle;
import com.zephyr.Backend.SavingManager;
import com.zephyr.Zephyr;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.List;

public class HomeGUI extends Screen {
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
    private static final SoundEvent buttonClick = SoundEvents.UI_BUTTON_CLICK.value(); // button click sound

    // saved files
    private File SAVEFILE;
    private File HUDFILE;

    // Index of currently selected feature tab
    private int selectedFeatureIndex = 0;
    // Feature tab names displayed in left panel
    private final String[] features = {"Home", "Alerts", "Mining", "Rift", "Others"};

    public HomeGUI() {
        super(Text.of("Zephyr Menu"));
        Features.run();
        if(MinecraftClient.getInstance() != null) {
            SAVEFILE = new File(MinecraftClient.getInstance().runDirectory, "config/Zephyr/1.21.5/config.json");;
            HUDFILE = new File(MinecraftClient.getInstance().runDirectory, "config/Zephyr/1.21.5/hud_config.json");
        }
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Calculate top-left corner of the main panel to center it on screen
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;

        drawBlurAroundPanel(context, panelX, panelY);

        // Draw background rectangles for both panels and separator line
        drawPanelsBackground(context, panelX, panelY);
        // Draw feature list on left panel
        drawLeftPanelFeatures(context, panelX, panelY, mouseX, mouseY);
        // Draw feature-specific content on right panel
        drawRightPanelContent(context, panelX + LEFT_PANEL_WIDTH + RIGHT_PANEL_PADDING_X, panelY + RIGHT_PANEL_PADDING_Y, mouseX, mouseY);
    }

    private void drawBlurAroundPanel(DrawContext context, int panelX, int panelY) {
        int panelRight = panelX + PANEL_WIDTH;
        int panelBottom = panelY + PANEL_HEIGHT;

        int blurColor = 0xCC000000;

        // Top
        context.fill(0, 0, width, panelY, blurColor);
        // Bottom
        context.fill(0, panelBottom, width, height, blurColor);
        // Left
        context.fill(0, panelY, panelX, panelBottom, blurColor);
        // Right
        context.fill(panelRight, panelY, width, panelBottom, blurColor);
    }

    // Draw background rectangles for left and right panels, and the separator line
    private void drawPanelsBackground(DrawContext context, int x, int y) {
        context.fill(x, y, x + LEFT_PANEL_WIDTH,
                y + PANEL_HEIGHT, 0xE6040404); // Left panel bg
        context.fill(x + LEFT_PANEL_WIDTH, y,
                x + PANEL_WIDTH, y + PANEL_HEIGHT, 0xE6040404); // Right panel bg
        context.fill(x + LEFT_PANEL_WIDTH, y,
                x + LEFT_PANEL_WIDTH + 1, y + PANEL_HEIGHT,
                0xFF3F76E4); // Blue separator line
    }

    // Draw the list of feature tabs in the left panel
    private void drawLeftPanelFeatures(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int yStart = y + LEFT_PANEL_PADDING_Y;

        for (int i = 0; i < features.length; i++) {
            int yPos = yStart + i * FEATURE_TEXT_SPACING;

            // Check if mouse is hovering over this feature text
            boolean isHover = mouseX >= x + LEFT_PANEL_PADDING_X
                    && mouseX <= x + LEFT_PANEL_WIDTH - LEFT_PANEL_PADDING_X
                    && mouseY >= yPos
                    && mouseY <= yPos + FEATURE_TEXT_HEIGHT;

            int color;
            Text text = Text.literal(features[i]);

            // Highlight currently selected feature with underline and blue color
            if (i == selectedFeatureIndex) {
                text = text.copy().formatted(Formatting.UNDERLINE);
                color = 0xFF3F76E4;
            } else if (isHover) {
                // Highlight hovered feature with blue color
                color = 0xFF3F76E4;
            } else {
                // Default white color
                color = 0xFFFFFFFF;
            }

            // Draw feature text label
            context.drawTextWithShadow(textRenderer, text, x + LEFT_PANEL_PADDING_X, yPos, color);
        }

        // Draw GUI title and creator info at the top of the left panel
        drawTitleAndCreator(context, x);
    }

    // Draw content for the right panel based on the selected feature tab
    private void drawRightPanelContent(DrawContext context, int x, int y, int mouseX, int mouseY) {
        String currentFeature = features[selectedFeatureIndex];
        // Draw the feature title with underline and bold styling
        Text title = Text.literal(currentFeature)
                .styled(style -> style.withColor(0x3F76E4).withUnderline(true).withBold(true));

        // Draw the feature tab title at the top
        context.drawTextWithShadow(textRenderer, title, x, y, 0xFF3F76E4);

        int offsetY = y + 20;

        // If "Home" feature selected, draw Home specific content
        if ("Home".equals(currentFeature)) {
            drawHomePanelContent(context, x, offsetY, mouseX, mouseY);
            return;
        }

        // Get toggles and dropdowns for the current feature
        List<FeaturesToggle> toggles = Features.getFeatureSettings().get(currentFeature);
        //Map<String, List<FeatureDropdown>> dropdownList = FeatureDropdown.getDropdownsByFeature();
        //List<FeatureDropdown> dropdowns = dropdownList.get(currentFeature);

        // If no toggles available, show "WIP"
        if (toggles == null || toggles.isEmpty()) {
            context.drawTextWithShadow(textRenderer, "WIP", x, offsetY, 0xAAAAAA);
            return;
        }

        // Keep track of toggles already drawn inside dropdowns to avoid duplicates
        /*Set<Settings> drawnInDropdown = new HashSet<Settings>();
        if (dropdowns != null) {
            for (FeatureDropdown dropdown : dropdowns) {
                dropdown.draw(mc, fontRendererObj, x, offsetY, mouseX, mouseY);
                drawnInDropdown.addAll(dropdown.getToggles());
                offsetY += dropdown.getHeight(fontRendererObj) + 10;
            }
        }*/

        // Draw toggles not contained inside dropdowns individually
        if (toggles != null) {
            for (FeaturesToggle toggle : toggles) {
                //if (drawnInDropdown.contains(toggle)) continue;

                // Set toggle position further right for better alignment
                int toggleX = x + 20;
                toggle.setX(toggleX);
                toggle.setY(offsetY);

                // Toggle label color depends on toggle state
                int labelColor = toggle.getValue() ? 0xFF3F76E4 : 0xFF555555;
                context.drawTextWithShadow(textRenderer, toggle.getLabel(), x, offsetY + 4, labelColor);
                toggle.draw(context, mouseX, mouseY);

                offsetY += toggle.getHeight();

                // Draw optional description in smaller scaled font below toggle label
                String desc = toggle.getDescription();
                if (desc != null && !desc.isEmpty()) {
                    context.getMatrices().push();
                    context.getMatrices().scale(0.77F, 0.77F, 1.0F);
                    int descColor = toggle.getValue() ? 0xFFFFFFFF : 0xFF555555;
                    context.drawTextWithShadow(textRenderer,
                            desc,
                            (int) ((x + 4) / 0.77F),
                            (int) ((offsetY + 2) / 0.77F),
                            descColor);
                    context.getMatrices().pop();
                    offsetY += 14;
                } else {
                    offsetY += 5;
                }
            }
        }
    }

    // Draw content specific to the "Home" tab
    private void drawHomePanelContent(DrawContext context, int x, int offsetY, int mouseX, int mouseY) {
        // Display version string
        context.drawTextWithShadow(textRenderer,"Zephyr v" + Zephyr.MOD_VERSION, x, offsetY, 0xFF3F76E4);
        offsetY += 18;

        int rowHeight = 14;
        int rowY = offsetY;
        int textY = rowY + (rowHeight - textRenderer.fontHeight) / 2;
        context.drawTextWithShadow(textRenderer, "Edit HUD Location", x, textY, 0xFF3F76E4);

        // Position and draw the "Edit" button for HUD editing
        //editButton.setPosition(x + 350, rowY);
        //editButton.draw(mc, mouseX, mouseY);
    }

    // Draw the title "SkyRats" and the authors below it
    private void drawTitleAndCreator(DrawContext context, int panelX) {
        int yPos = (height - PANEL_HEIGHT) / 2 + 10;
        // Compose the title text with bold + underline formatting
        Text title = Text.literal("Zephyr").formatted(Formatting.BOLD, Formatting.UNDERLINE);
        context.drawTextWithShadow(textRenderer, title, panelX + LEFT_PANEL_PADDING_X, yPos, 0xFF3F76E4);

        // Compose the author text with bold formatting
        Text author = Text.literal("By Sunaio & A_Blender_").formatted(Formatting.BOLD);

        context.getMatrices().push();
        context.getMatrices().scale(0.87F, 0.87F, 1.0F);
        // Adjust position for scaling
        int scaledX = (int) ((panelX + LEFT_PANEL_PADDING_X) / 0.87F);
        int scaledY = (int) ((yPos + 14) / 0.87F);

        context.drawTextWithShadow(textRenderer, author, scaledX, scaledY, 0xFF3F76E4);
        context.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        // Only handle left click here
        if (mouseButton != 0) {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        // Check if edit button is hovered
        /*if ("Home".equals(features[selectedFeatureIndex]) && editButton.isHovered(mouseX, mouseY)) {
            editButton.onClick();
            return true;
        }*/

        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;

        // Check if the click was on the left panel feature list
        if (handleLeftPanelClick((int) mouseX, (int) mouseY, panelX, panelY)) return true;
        // Check if the click was on the right panel content (toggles, dropdowns)
        if (handleRightPanelClick((int) mouseX, (int) mouseY)) return true;

        return super.mouseClicked(mouseX, mouseY, mouseButton);
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
        /*Map<String, List<FeatureDropdown>> dropdownMap = FeatureDropdown.getDropdownsByFeature();
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
        }*/

        // Check toggles if clicked and toggle them
        List<FeaturesToggle> toggles = Features.getFeatureSettings().get(features[selectedFeatureIndex]);
        if (toggles != null) {
            for (FeaturesToggle toggle : toggles) {
                if (toggle.isHovered(mouseX, mouseY)) {
                    toggle.toggle();
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.playSound(
                            buttonClick, 1.0F, 1.0F
                    );
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void removed() {
        // Save feature settings to config file on GUI close
        SavingManager.save(Features.getFeatureSettings(), SAVEFILE);
        super.removed();
    }
}