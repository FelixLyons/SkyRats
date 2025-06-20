package com.Zephyr.Core.Features;

import com.Zephyr.Core.GUI.SettingsManager;
import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerTracker {
    private static final File timerTracker = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/time.json");
    private static final Map<String, TimeData> cooldowns = new HashMap<String, TimeData>();

    // Data class representing one cooldown
    public static class TimeData {
        @Expose
        public String name;
        @Expose
        public long endTime;

        public TimeData(String name, long endTime) {
            this.name = name;
            this.endTime = endTime;
        }
    }

    // Load cooldown time
    public static void load() {
        Map<String, List<TimeData>> loaded = SettingsManager.load(timerTracker, TimeData.class);
        cooldowns.clear();
        if(loaded != null) {
            if(loaded.containsKey("cooldowns")) {
                for(TimeData time : loaded.get("cooldowns")) {
                    cooldowns.put(time.name, time);
                }
            }
        }
    }

    // Save cooldown time
    public static void save() {
        List<TimeData> list = new ArrayList<TimeData>(cooldowns.values());
        Map<String, List<TimeData>> toSave = new HashMap<String, List<TimeData>>();
        toSave.put("cooldowns", list);
        SettingsManager.save(toSave, timerTracker);
    }

    // Start a cooldown
    public static void startCooldown(String name, int seconds) {
        long endTime = System.currentTimeMillis() + seconds * 1000L;
        cooldowns.put(name, new TimeData(name, endTime));
        save();
    }

    // Check if cooldown is still running
    public static boolean isCooldownActive(String name) {
        TimeData cd = cooldowns.get(name);
        return cd != null && System.currentTimeMillis() < cd.endTime;
    }

    // Get seconds left on cooldown
    public static int getCooldownTimeLeft(String name) {
        TimeData cd = cooldowns.get(name);
        if (cd == null) return 0;
        long diff = cd.endTime - System.currentTimeMillis();
        return diff > 0 ? (int)(diff / 1000) : 0;
    }

    // Remove a cooldown
    public static void clearCooldown(String name) {
        cooldowns.remove(name);
        save();
    }
}
