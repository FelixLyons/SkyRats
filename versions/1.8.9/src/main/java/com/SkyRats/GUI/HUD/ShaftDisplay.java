package com.SkyRats.GUI.HUD;

import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.Mineshafts.ShaftTypes;
import com.SkyRats.Core.GUI.FeatureSettings;
import com.SkyRats.Core.GUI.MovableUIs;
import com.SkyRats.GUI.EditGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ShaftDisplay extends MovableUIs {
    private final MineshaftTracker tracker;

    public ShaftDisplay(MineshaftTracker tracker) {
        super("Mineshaft Tracker");
        this.tracker = tracker;
        this.setFeatureLabel("Mineshaft Tracker HUD");

        // Default HUD size
        this.width = 100;
        this.height = 195;
    }

    @Override
    public void render(Minecraft mc) {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        if(!FeatureSettings.isFeatureEnabled("HUD", "Mineshaft Tracker HUD")) return;
        boolean isEditing = mc.currentScreen instanceof EditGUI;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        int drawY = 0;

        mc.fontRendererObj.drawStringWithShadow("§l§6Mineshaft Tracker", 0, drawY, 0xFFFFFF);
        drawY += 12;

        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<Map.Entry<ShaftTypes, Integer>>();
        for(ShaftTypes type : ShaftTypes.values()) {
            int count = tracker.getCount(type);
            if(isEditing || count > 0) {
                entries.add(new AbstractMap.SimpleEntry<ShaftTypes, Integer>(type, count));
            }
        }

        Collections.sort(entries, new Comparator<Map.Entry<ShaftTypes, Integer>>() {
            public int compare(Map.Entry<ShaftTypes, Integer> a, Map.Entry<ShaftTypes, Integer> b) {
                int cmp = b.getValue() - a.getValue();
                if(cmp != 0) {
                    return cmp;
                }
                return a.getKey().name().compareTo(b.getKey().name());
            }
        });

        for(Map.Entry<ShaftTypes, Integer> entry : entries) {
            ShaftTypes type = entry.getKey();
            int count = entry.getValue();
            String color = (count > 0) ? type.getColor() : "§7";

            mc.fontRendererObj.drawStringWithShadow(
                    color + type.name() + ": §e" + count,
                    0, drawY,
                    0xFFFFFF
            );
            drawY += 10;
        }

        drawY += 4;

        mc.fontRendererObj.drawStringWithShadow("§7Total: §a" + tracker.getTotalShafts(), 0, drawY, 0xFFFFFF);
        drawY += 10;
        mc.fontRendererObj.drawStringWithShadow("§7Since §dJASPER§7: §c" + tracker.getSinceJasper(), 0, drawY, 0xFFFFFF);

        GL11.glPopMatrix();

        if(isEditing) {
            int borderColor = 0xFF00BFFF;
            int padding = 3;

            Gui.drawRect(x - padding, y - padding, x + getScaledWidth() + padding, y - padding + 1, borderColor); // Top
            Gui.drawRect(x - padding, y + getScaledHeight() + padding - 1, x + getScaledWidth() + padding, y + getScaledHeight() + padding, borderColor); // Bottom
            Gui.drawRect(x - padding, y - padding, x - padding + 1, y + getScaledHeight() + padding, borderColor); // Left
            Gui.drawRect(x + getScaledWidth() + padding - 1, y - padding, x + getScaledWidth() + padding, y + getScaledHeight() + padding, borderColor); // Right
        }
    }
}
