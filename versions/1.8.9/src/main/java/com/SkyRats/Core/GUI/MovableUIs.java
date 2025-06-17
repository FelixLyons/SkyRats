package com.SkyRats.Core.GUI;

import net.minecraft.client.Minecraft;

public abstract class MovableUIs {
    protected String name;
    protected int x = 10, y = 10;
    protected int width = 100, height = 20;

    public boolean dragging = false;
    public int dragX, dragY;

    public abstract void render(Minecraft mc);

    public MovableUIs(String name) {
        this.name = name;
    }

    public void renderDummy(Minecraft mc) {
        render(mc);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void handleDrag(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
