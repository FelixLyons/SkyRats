package com.SkyRats.GUI.HUD;

import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.Mineshafts.ShaftTypes;
import com.SkyRats.Core.GUI.MovableUIs;
import com.SkyRats.GUI.EditGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.*;

public class ShaftDisplay extends MovableUIs {
    private final MineshaftTracker tracker;

    public ShaftDisplay(MineshaftTracker tracker) {
        super("Mineshaft Tracker");
        this.tracker = tracker;

        // Default HUD size
        this.width = 100;
        this.height = 195;
    }

    @Override
    public void render(Minecraft mc) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        // Check if we're currently inside the Edit GUI screen
        boolean isEditing = mc.currentScreen instanceof EditGUI;

        int drawY = y;

        // Draw the title of the HUD
        mc.fontRendererObj.drawStringWithShadow("§l§6Mineshaft Tracker", x, drawY, 0xFFFFFF);
        drawY += 12;

        if (isEditing) {
            // Draw 1-pixel thick white border (hitbox) for dragging/editing visualization
            int borderColor = 0xFF00BFFF; // light blue
            int padding = 3;

            Gui.drawRect(x - padding, y - padding, x + width + padding, y - padding + 1, borderColor);               // Top
            Gui.drawRect(x - padding, y + height + padding - 1, x + width + padding, y + height + padding, borderColor); // Bottom
            Gui.drawRect(x - padding, y - padding, x - padding + 1, y + height + padding, borderColor);              // Left
            Gui.drawRect(x + width + padding - 1, y - padding, x + width + padding, y + height + padding, borderColor);  // Right
        }

        // Build a list of shaft types and their respective counts
        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<Map.Entry<ShaftTypes, Integer>>();
        for (ShaftTypes type : ShaftTypes.values()) {
            int count = tracker.getCount(type);

            // If we're editing, show all shafts (even count = 0)
            // Otherwise, only show shafts that have at least 1
            if (isEditing || count > 0) {
                entries.add(new AbstractMap.SimpleEntry<ShaftTypes, Integer>(type, count));
            }
        }

        // Sort entries by count (descending), then by name (ascending)
        Collections.sort(entries, new Comparator<Map.Entry<ShaftTypes, Integer>>() {
            public int compare(Map.Entry<ShaftTypes, Integer> a, Map.Entry<ShaftTypes, Integer> b) {
                int cmp = b.getValue() - a.getValue(); // higher count first
                return (cmp != 0) ? cmp : a.getKey().name().compareTo(b.getKey().name());
            }
        });

        // Render each shaft type on the HUD
        for (Map.Entry<ShaftTypes, Integer> entry : entries) {
            ShaftTypes type = entry.getKey();
            int count = entry.getValue();

            // Gray color
            String color = (count > 0) ? type.getColor() : "§7";

            // Draw the formatted line
            mc.fontRendererObj.drawStringWithShadow(
                    color + type.name() + ": §e" + count,
                    x, drawY,
                    0xFFFFFF
            );

            drawY += 10; // Move down for next line
        }

        // Add spacing before totals
        drawY += 4;

        // Draw total shafts found
        mc.fontRendererObj.drawStringWithShadow(
                "§7Total: §a" + tracker.getTotalShafts(),
                x, drawY,
                0xFFFFFF
        );

        drawY += 10;

        // Draw shafts found since last Jasper
        mc.fontRendererObj.drawStringWithShadow(
                "§7Since §dJASPER§7: §c" + tracker.getSinceJasper(),
                x, drawY,
                0xFFFFFF
        );
    }
}
