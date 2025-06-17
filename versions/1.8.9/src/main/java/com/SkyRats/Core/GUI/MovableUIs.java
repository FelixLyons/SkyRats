package com.SkyRats.Core.GUI;

import com.SkyRats.GUI.HUD.HUDManager;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.List;

public abstract class MovableUIs {
    protected String name;
    protected int x = 10, y = 10;
    protected int width = 100, height = 20;
    protected float scale = 1.0F;
    protected String featureLabel;

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
        if(isHovered(mouseX, mouseY)) {
            if (button == 0) { // left-click starts dragging
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            } else if (button == 1) { // right-click triggers reset (to be overridden)
                setPosition(10, 10);
                setScale(1.0F);;
            }
        }
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void handleDrag(int mouseX, int mouseY) {
        if(dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + getScaledWidth() &&
                mouseY >= y && mouseY <= y + getScaledHeight();
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = Math.max(0.5F, Math.min(3.0F, scale)); // limit scaling
    }

    // Size after scaling
    public int getScaledWidth() {
        return (int)(width * scale);
    }

    public int getScaledHeight() {
        return (int)(height * scale);
    }

    public String getFeatureLabel() {
        return featureLabel;
    }

    public void setFeatureLabel(String label) {
        this.featureLabel = label;
    }
}
