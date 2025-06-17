package com.SkyRats.Core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class EditButton {
    private int x, y;
    private final int width = 50;
    private final int height = 14;

    private final int bgColor = 0xFF2A2A2A;
    private final int borderColor = 0xFF444444;
    private final int textColor = 0xFFFFFFFF;

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

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        boolean hovered = isHovered(mouseX, mouseY);
        // White text always, but can change if you want
        int currentTextColor = hovered ? 0xFF3F76E4 : 0xFFFFFFFF;

        // Background
        drawRect(x, y, x + width, y + height, bgColor);
        drawRect(x, y, x + width, y + 1, borderColor);
        drawRect(x, y + height - 1, x + width, y + height, borderColor);
        drawRect(x, y, x + 1, y + height, borderColor);
        drawRect(x + width - 1, y, x + width, y + height, borderColor);

        // Draw centered text
        FontRenderer font = mc.fontRendererObj;
        int textWidth = font.getStringWidth(label);
        font.drawString(label, x + (width - textWidth) / 2, y + 3, currentTextColor);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void onClick() {
        action.run();
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        net.minecraft.client.gui.Gui.drawRect(left, top, right, bottom, color);
    }
}
