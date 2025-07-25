package com.zephyr.Backend.GUIs;

import com.google.gson.annotations.Expose;
import net.minecraft.client.gui.DrawContext;

public class FeaturesToggle {
    private int x, y, num;
    private final int width = 50;
    private final int height = 14;
    private final int toggleWidth = 24;
    private final int toggleHeight = height - 5;
    private int toggleX;
    private int toggleY;

    @Expose
    private String label;
    @Expose private boolean value;
    private String description;

    public FeaturesToggle(String label, String description, boolean defaultValue) {
        this.label = label;
        this.description = description;
        this.value = defaultValue;
    }

    // Getters and Setters
    public String getLabel() {
        return label;
    }

    public boolean getValue() {
        return value;
    }

    //Never used here (useless)
    public int getNum() {
        return num;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void toggle() {
        this.value = !value;
    }

    // GUI Positioning
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        toggleX = this.x + 350;
    }
    public void setY(int y) {
        this.y = y;
        toggleY = this.y + (height - toggleHeight) / 2; // center vertically
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    // Rendering logic
    public void draw(DrawContext context, int mouseX, int mouseY) {
        // Button background, shape, size, and color
        context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, 0xFF2A2A2A);
        context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + 1, 0xFF444444);
        context.fill(toggleX, toggleY + toggleHeight - 1, toggleX + toggleWidth, toggleY + toggleHeight, 0xFF444444);
        context.fill(toggleX, toggleY, toggleX + 1, toggleY + toggleHeight, 0xFF444444);
        context.fill(toggleX + toggleWidth - 1, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, 0xFF444444);

        // Switch size and color
        int thumbWidth = toggleWidth / 2;
        int thumbX = value ? (toggleX + thumbWidth) : toggleX;
        int thumbColor = value ? 0xFF3F76E4 : 0xFFD3D3D3; // Blue if on, gray if off
        context.fill(thumbX, toggleY, thumbX + thumbWidth, toggleY + toggleHeight, thumbColor);
    }

    // Button hitbox
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= toggleX && mouseX <= toggleX + toggleWidth &&
                mouseY >= toggleY && mouseY <= toggleY + toggleHeight;
    }

    // Draw rectangle on said coordinates
    private void fillRect(DrawContext context, int left, int top, int right, int bottom, int color) {
        context.fill(left, top, right, bottom, color);
    }
}
