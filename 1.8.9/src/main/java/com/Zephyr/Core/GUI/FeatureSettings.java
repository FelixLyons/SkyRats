package com.Zephyr.Core.GUI;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.*;

public class FeatureSettings {
    private static final File SETTINGS_FILE = new File(Minecraft.getMinecraft().mcDataDir, "config/Zephyr/1.8.9/config.json");

    private static final Map<String, List<Settings>> featureSettings = new HashMap<String, List<Settings>>();

    public static void run() {
        // Load previously saved settings into memory
        Map<String, List<Settings>> loaded = SettingsManager.load(SETTINGS_FILE, Settings.class);
        if(loaded != null) {
            featureSettings.putAll(loaded);
        }

        // Register defaults (merge with existing ones, update descriptions)
        registerDefaults();

        // Save merged settings
        SettingsManager.save(featureSettings, SETTINGS_FILE);
    }

    // Default settings
    private static void registerDefaults() {
        register("Mining", Arrays.asList(
                setting("Mineshaft Tracker", "Tracks all mineshaft types that you have entered", false)
        ));

        register("Alerts", Arrays.asList(
                setting("Mining Ability Cooldown Notification", "Alerts you when your ability is off cooldown", false),
                setting("Reminder to Switch Ability Notification", "Alerts you to switch ability if you switched after leaving mineshaft", false)
        ));

        register("Rift", Arrays.asList(
                setting("Split or Steal Tracker", "Tracks the cooldown of Split or Steal", false)
        ));

        register("HUD", Arrays.asList(
                setting("Mineshaft Tracker HUD", "Display HUD for Mineshafts, REQUIRES TRACKER TO BE ENABLED", false),
                new Settings("Split or Steal HUD", "Display time HUD for Split or Steal, REQUIRES TRACKER TO BE ENABLED", false)
        ));
    }

    // Add settings onto the file, merging and updating descriptions
    private static void register(String featureName, List<Settings> defaults) {
        List<Settings> existing = featureSettings.get(featureName);

        if(existing == null) {
            // No existing settings, just add defaults
            featureSettings.put(featureName, new ArrayList<Settings>(defaults));
        } else {
            for(Settings def : defaults) {
                boolean found = false;
                for (Settings e : existing) {
                    if (e.getLabel().equalsIgnoreCase(def.getLabel())) {
                        // Update description if missing or empty
                        if (e.getDescription() == null || e.getDescription().isEmpty()) {
                            e.setDescription(def.getDescription());
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // New setting not found, add it
                    existing.add(def);
                }
            }
        }
    }

    private static Settings setting(String label, String description, boolean defaultValue) {
        return new Settings(label, description, defaultValue);
    }

    // Return the setting name and their values
    public static Map<String, List<Settings>> getFeatureSettings() {
        return featureSettings;
    }

    // Checks if feature is enabled
    public static boolean isFeatureEnabled(String feature, String label) {
        List<Settings> settingsList = featureSettings.get(feature);
        if(settingsList != null) {
            for (Settings s : settingsList) {
                if (s.getLabel().equalsIgnoreCase(label)) {
                    return s.getValue();
                }
            }
        }
        return false;
    }
}
