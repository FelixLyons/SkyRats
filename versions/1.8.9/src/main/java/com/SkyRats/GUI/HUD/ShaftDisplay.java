package com.SkyRats.GUI.HUD;

import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.Mineshafts.ShaftTypes;
import com.SkyRats.Core.GUI.MovableUIs;
import net.minecraft.client.Minecraft;

import java.util.*;

public class ShaftDisplay extends MovableUIs {
    private final MineshaftTracker tracker;

    public ShaftDisplay(MineshaftTracker tracker) {
        super("Mineshaft Tracker");
        this.tracker = tracker;
        this.width = 150;
        this.height = 150;
    }

    // Renders the HUD on screen
    @Override
    public void render(Minecraft mc) {
        int drawY = y;
        // Title
        mc.fontRendererObj.drawStringWithShadow("§l§6Mineshaft Tracker", x, drawY, 0xFFFFFF);
        drawY += 12;

        // Getting the numbers
        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<Map.Entry<ShaftTypes, Integer>>();
        for (ShaftTypes type : ShaftTypes.values()) {
            int count = tracker.getCount(type);
            if (count > 0) {
                entries.add(new AbstractMap.SimpleEntry<ShaftTypes, Integer>(type, count));
            }
        }

        // Sort from highest to lowest
        Collections.sort(entries, new Comparator<Map.Entry<ShaftTypes, Integer>>() {
            @Override
            public int compare(Map.Entry<ShaftTypes, Integer> a, Map.Entry<ShaftTypes, Integer> b) {
                return b.getValue() - a.getValue();  // descending order
            }
        });

        // Formatting and print name and number
        for (Map.Entry<ShaftTypes, Integer> entry : entries) {
            ShaftTypes type = entry.getKey();
            int count = entry.getValue();
            String line = type.getColor() + type.name() + ": §e" + count;
            mc.fontRendererObj.drawStringWithShadow(line, x, drawY, 0xFFFFFF);
            drawY += 10;
        }

        // Total
        drawY += 4;
        mc.fontRendererObj.drawStringWithShadow("§7Total: §a" + tracker.getTotalShafts(), x, drawY, 0xFFFFFF);

        // Since last Jasper
        drawY += 10;
        mc.fontRendererObj.drawStringWithShadow("§7Since §dJASPER§7: §c" + tracker.getSinceJasper(), x, drawY, 0xFFFFFF);
    }
}
