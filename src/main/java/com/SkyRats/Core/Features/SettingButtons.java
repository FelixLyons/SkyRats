package com.SkyRats.Core.Features;

import com.google.gson.annotations.Expose;

public class SettingButtons {
    private int x, y;
    private final int width = 50;
    private final int height = 14;

    @Expose private String label;
    @Expose private boolean value;

    public SettingButtons(String label, boolean defaultValue) {
        this.label = label;
        this.value = defaultValue;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(net.minecraft.client.Minecraft mc, int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(label, x, y + 3, 0xFFFFFF);
        int toggleX = x + 280;
        int toggleY = y;
        int toggleColor = value ? 0xFF00FF00 : 0xFFFF0000;

        drawRect(toggleX, toggleY, toggleX + width, toggleY + height, 0xFF333333);
        drawRect(toggleX + 2, toggleY + 2, toggleX + width - 2, toggleY + height - 2, toggleColor);

        // Draw On/Off text centered inside the toggle
        String toggleText = value ? "On" : "Off";
        int textWidth = mc.fontRendererObj.getStringWidth(toggleText);
        int textX = toggleX + (width - textWidth) / 2;
        int textY = toggleY + (height - 8) / 2;  // 8 is approx text height

        mc.fontRendererObj.drawString(toggleText, textX, textY, 0xFFFFFF);
    }

    public String getLabel() {
        return label;
    }

    public boolean getValue() {
        return value;
    }

    public void toggle() {
        value = !value;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        int toggleX = x + 280;
        return mouseX >= toggleX && mouseX <= toggleX + width &&
                mouseY >= y && mouseY <= y + height;
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        net.minecraft.client.gui.Gui.drawRect(left, top, right, bottom, color);
    }


}
