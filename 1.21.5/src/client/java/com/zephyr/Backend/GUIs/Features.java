package com.zephyr.Backend.GUIs;

import com.zephyr.Backend.SavingManager;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.util.*;

public class Features {
    private static File SETTINGS_FILE;

    private static final Map<String, List<FeaturesToggle>> featureSettings = new HashMap<>();

    public static void run() {
        if(MinecraftClient.getInstance() != null) {
            SETTINGS_FILE = new File(MinecraftClient.getInstance().runDirectory, "config/Zephyr/1.21.5/config.json");
        }
        // Load previously saved settings into memory
        Map<String, List<FeaturesToggle>> loaded = SavingManager.load(SETTINGS_FILE, FeaturesToggle.class);
        if(loaded != null) {
            featureSettings.putAll(loaded);
        }

        // Register defaults (merge with existing ones, update descriptions)
        registerDefaults();

        // Save merged settings
        SavingManager.save(featureSettings, SETTINGS_FILE);
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
                setting("Split or Steal HUD", "Display time HUD for Split or Steal, REQUIRES TRACKER TO BE ENABLED", false)
        ));

        register("Others", Arrays.asList(
                setting("1.8.9 Glass Pane", "1.8.9 Glass Pane Hitbox", false)
        ));
    }

    // Add settings onto the file, merging and updating descriptions
    private static void register(String featureName, List<FeaturesToggle> defaults) {
        List<FeaturesToggle> existing = featureSettings.get(featureName);

        if(existing == null) {
            // No existing settings, just add defaults
            featureSettings.put(featureName, new ArrayList<FeaturesToggle>(defaults));
        } else {
            for(FeaturesToggle def : defaults) {
                boolean found = false;
                for (FeaturesToggle e : existing) {
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

    private static FeaturesToggle setting(String label, String description, boolean defaultValue) {
        return new FeaturesToggle(label, description, defaultValue);
    }

    // Return the setting name and their values
    public static Map<String, List<FeaturesToggle>> getFeatureSettings() {
        return featureSettings;
    }

    // Checks if feature is enabled
    public static boolean isFeatureEnabled(String feature, String label) {
        List<FeaturesToggle> settingsList = featureSettings.get(feature);
        if(settingsList != null) {
            for (FeaturesToggle s : settingsList) {
                if (s.getLabel().equalsIgnoreCase(label)) {
                    return s.getValue();
                }
            }
        }
        return false;
    }
}
