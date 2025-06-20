package com.Zephyr.Core.GUI;

import net.minecraft.client.Minecraft;

public abstract class MovableUIs {
    protected String name;
    protected int x = 10, y = 10;
    protected int width = 100, height = 20; // Base size (before scaling)
    protected float scale = 1.0F; // Scale factor for size (1.0 = normal size)
    protected String featureLabel;

    // Dragging state variables
    public boolean dragging = false;
    public int dragX, dragY;

    // Render method to be implemented by subclasses
    public abstract void render(Minecraft mc);

    public MovableUIs(String name) {
        this.name = name;
    }

    // Renders the HUD dummy
    public void renderDummy(Minecraft mc) {
        render(mc);
    }

    /* Called when a mouse button is clicked.
    Starts dragging if left-clicked on HUD, or resets scale to default if right-clicked.
    */
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(isHovered(mouseX, mouseY)) {
            if (button == 0) { // left-click starts dragging
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            } else if (button == 1) { // right-click triggers scaling reset
                setScale(1.0F);;
            }
        }
    }

    // Called when mouse button is released.
    public void mouseReleased() {
        dragging = false;
    }

    // Updates position while dragging based on current mouse coordinates.
    public void handleDrag(int mouseX, int mouseY) {
        if(dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    /* Checks if a given point (mouseX, mouseY) is hovering over this HUD element,
       taking into account its current scaled size.
    */
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

    // Sets the scale factor, clamped between 0.5 (small) and 3.0 (large).
    public void setScale(float scale) {
        this.scale = Math.max(0.5F, Math.min(3.0F, scale)); // limits scaling
    }

    // Calculates the width after applying the current scale factor.
    public int getScaledWidth() {
        return (int)(width * scale);
    }

    // Calculates the height after applying the current scale factor.
    public int getScaledHeight() {
        return (int)(height * scale);
    }

    // Gets the feature label (used for settings management).
    public String getFeatureLabel() {
        return featureLabel;
    }

    // Sets the feature label.
    public void setFeatureLabel(String label) {
        this.featureLabel = label;
    }
}
