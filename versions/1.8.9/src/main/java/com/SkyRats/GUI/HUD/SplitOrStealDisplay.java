package com.SkyRats.GUI.HUD;

import com.SkyRats.Core.Features.TimerTracker;
import com.SkyRats.Core.GUI.MovableUIs;
import com.SkyRats.GUI.EditGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

public class SplitOrStealDisplay extends MovableUIs {

    public SplitOrStealDisplay() {
        super("SplitOrSteal");
    }

    @Override
    public void render(Minecraft mc) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        boolean isEditing = mc.currentScreen instanceof EditGUI;

        String text;
        int seconds = TimerTracker.getCooldownTimeLeft("SplitOrSteal");

        if (seconds <= 0) {
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

        if (isEditing) {
            int borderColor = 0xFF00BFFF; // Light Blue

            // Top border
            Gui.drawRect(x - 2, y - 2, x + width + 2, y - 1, borderColor);
            // Bottom border
            Gui.drawRect(x - 2, y + height + 1, x + width + 2, y + height + 2, borderColor);
            // Left border
            Gui.drawRect(x - 2, y - 2, x - 1, y + height + 2, borderColor);
            // Right border
            Gui.drawRect(x + width + 1, y - 2, x + width + 2, y + height + 2, borderColor);
        }

        mc.fontRendererObj.drawStringWithShadow(text, x, y, 0xFFFFFF);
    }
}