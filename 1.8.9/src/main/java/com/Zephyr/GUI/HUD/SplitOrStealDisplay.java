package com.Zephyr.GUI.HUD;

import com.Zephyr.Core.Features.TimerTracker;
import com.Zephyr.Core.GUI.FeatureSettings;
import com.Zephyr.Core.GUI.MovableUIs;
import com.Zephyr.GUI.EditGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class SplitOrStealDisplay extends MovableUIs {

    public SplitOrStealDisplay() {
        super("Split Or Steal");
        this.setFeatureLabel("Split or Steal HUD");
    }

    @Override
    public void render(Minecraft mc) {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        // Only render if the feature is enabled
        if(!FeatureSettings.isFeatureEnabled("HUD", "Split or Steal HUD")) return;
        boolean isEditing = mc.currentScreen instanceof EditGUI;

        String text;
        int seconds = TimerTracker.getCooldownTimeLeft("Split_Or_Steal");

        // Format text differently based on cooldown availability
        if(seconds <= 0) {
            text = EnumChatFormatting.GREEN + "Split Or Steal: Available";
        } else {
            int h = seconds / 3600;
            int m = (seconds % 3600) / 60;
            int s = seconds % 60;
            text = EnumChatFormatting.RED + "Split Or Steal: " +
                    (h > 0 ? h + "h " : "") +
                    (m > 0 ? m + "m " : "") +
                    s + "s";
        }

        // Update width based on text
        this.width = mc.fontRendererObj.getStringWidth(text) + 4;
        this.height = mc.fontRendererObj.FONT_HEIGHT;

        // Scaling
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        // Draw the text at (0, 0)
        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, 0xFFFFFF);

        // If editing mode (HUD editor GUI), draw a border (hitbox) around the HUD element
        if(isEditing) {
            int borderColor = 0xFF00BFFF; // Light Blue
            int padding = 2;

            // Draw borders scaled
            Gui.drawRect(-padding, -padding, width + padding, -padding + 1, borderColor); // Top
            Gui.drawRect(-padding, height + padding - 1, width + padding, height + padding, borderColor); // Bottom
            Gui.drawRect(-padding, -padding, -padding + 1, height + padding, borderColor); // Left
            Gui.drawRect(width + padding - 1, -padding, width + padding, height + padding, borderColor); // Right
        }

        GL11.glPopMatrix();
    }
}