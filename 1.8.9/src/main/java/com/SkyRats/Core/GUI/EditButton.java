package com.SkyRats.Core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class EditButton {
    private int x, y;
    private final int width = 50;
    private final int height = 14;

    private final int bgColor = 0xFF2A2A2A; // Dark background
    private final int borderColor = 0xFF444444; // Gray border

    private final String label;
    private final Runnable action;

    public EditButton(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Draw the button on screen
    public void draw(Minecraft mc, int mouseX, int mouseY) {
        boolean hovered = isHovered(mouseX, mouseY);
        // White text always, but can change if you want
        int currentTextColor = hovered ? 0xFF3F76E4 : 0xFFFFFFFF; // blue when hovered

        // Background
        drawRect(x, y, x + width, y + height, bgColor);
        // Top border
        drawRect(x, y, x + width, y + 1, borderColor);
        // Bottom border
        drawRect(x, y + height - 1, x + width, y + height, borderColor);
        // Left border
        drawRect(x, y, x + 1, y + height, borderColor);
        // Right border
        drawRect(x + width - 1, y, x + width, y + height, borderColor);

        // Draw centered label text inside button
        FontRenderer font = mc.fontRendererObj;
        int textWidth = font.getStringWidth(label);
        font.drawString(label, x + (width - textWidth) / 2, y + 3, currentTextColor);
    }

    // Returns true if the mouse is over the button
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    // Button click
    public void onClick() {
        action.run();
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        net.minecraft.client.gui.Gui.drawRect(left, top, right, bottom, color);
    }
}
