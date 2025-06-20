package com.Zephyr.GUI.HUD;

import com.Zephyr.Core.Features.Mineshafts.MineshaftTracker;
import com.Zephyr.Core.Features.Mineshafts.ShaftTypes;
import com.Zephyr.Core.GUI.FeatureSettings;
import com.Zephyr.Core.GUI.MovableUIs;
import com.Zephyr.GUI.EditGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.*;

// HUD display for Mineshaft Tracker feature, shows counts of different shaft types and totals
public class ShaftDisplay extends MovableUIs {
    private final MineshaftTracker tracker;

    public ShaftDisplay(MineshaftTracker tracker) {
        super("Mineshaft Tracker");
        this.tracker = tracker;
        this.setFeatureLabel("Mineshaft Tracker HUD");

        // Default HUD size
        this.width = 120;
        this.height = 195;
    }

    @Override
    public void render(Minecraft mc) {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        // Only render if HUD feature is enabled
        if(!FeatureSettings.isFeatureEnabled("HUD", "Mineshaft Tracker HUD")) return;
        boolean isEditing = mc.currentScreen instanceof EditGUI;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        int drawY = 0;

        // Draw the HUD title
        mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GOLD +
                "" + EnumChatFormatting.UNDERLINE +
                "" + EnumChatFormatting.BOLD +
                "Mineshaft Tracker", 0, drawY, 0xFFFFFF);
        drawY += 12;

        // Collect shaft types and counts
        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<Map.Entry<ShaftTypes, Integer>>();
        for(ShaftTypes type : ShaftTypes.values()) {
            int count = tracker.getCount(type);
            // Show all types if editing (even count = 0), or only those with count > 0 otherwise
            if(isEditing || count > 0) {
                entries.add(new AbstractMap.SimpleEntry<ShaftTypes, Integer>(type, count));
            }
        }

        // Sort entries by count descending, then by name ascending
        Collections.sort(entries, new Comparator<Map.Entry<ShaftTypes, Integer>>() {
            public int compare(Map.Entry<ShaftTypes, Integer> a, Map.Entry<ShaftTypes, Integer> b) {
                int cmp = b.getValue() - a.getValue(); // Descending count
                if(cmp != 0) {
                    return cmp;
                }
                return a.getKey().name().compareTo(b.getKey().name()); // Ascending name
            }
        });

        // Draw each shaft type and its count with color formatting
        for(Map.Entry<ShaftTypes, Integer> entry : entries) {
            ShaftTypes type = entry.getKey();
            int count = entry.getValue();
            mc.fontRendererObj.drawStringWithShadow(
                    type.getColor() + type.name() + EnumChatFormatting.YELLOW + " " + count,
                    0, drawY,
                    0xFFFFFF
            );
            drawY += 10;
        }

        drawY += 4;

        // Total
        mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GRAY + "Total: " + EnumChatFormatting.GREEN +
                tracker.getTotalShafts(), 0, drawY, 0xFFFFFF);
        drawY += 10;
        // Total since Jasper
        mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GRAY + "Since " + EnumChatFormatting.LIGHT_PURPLE + "JASPER: " +
                EnumChatFormatting.RED + tracker.getSinceJasper(), 0, drawY, 0xFFFFFF);

        GL11.glPopMatrix();

        // If in editing mode (HUD Editor GUI), draw a border (hitbox) around the HUD
        if(isEditing) {
            int borderColor = 0xFF00BFFF; //light blue
            int padding = 3;

            Gui.drawRect(x - padding, y - padding, x + getScaledWidth() + padding, y - padding + 1, borderColor); // Top
            Gui.drawRect(x - padding, y + getScaledHeight() + padding - 1, x + getScaledWidth() + padding, y + getScaledHeight() + padding, borderColor); // Bottom
            Gui.drawRect(x - padding, y - padding, x - padding + 1, y + getScaledHeight() + padding, borderColor); // Left
            Gui.drawRect(x + getScaledWidth() + padding - 1, y - padding, x + getScaledWidth() + padding, y + getScaledHeight() + padding, borderColor); // Right
        }
    }
}
