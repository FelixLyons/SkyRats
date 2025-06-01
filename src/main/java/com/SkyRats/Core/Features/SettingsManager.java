package com.SkyRats.Core.Features;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class SettingsManager {
    private static final File SETTINGS_FILE = new File("SkyRats/settings.json");
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static final Map<String, List<SettingButtons>> featureSettings = new HashMap<String, List<SettingButtons>>();

    public static void register(String feature, List<SettingButtons> defaults) {
        if (!featureSettings.containsKey(feature)) {
            featureSettings.put(feature, defaults);
        }
    }

    public static List<SettingButtons> getFeatureToggles(String feature) {
        return featureSettings.getOrDefault(feature, Collections.<SettingButtons>emptyList());
    }

    //Create and save settings if there isn't any and other override current ones
    public static void save() {
        try {
            SETTINGS_FILE.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(SETTINGS_FILE);
            GSON.toJson(featureSettings, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Loads settings from existing file if there is one
    public static void load() {
        if (!SETTINGS_FILE.exists()) return;
        try {
            FileReader reader = new FileReader(SETTINGS_FILE);
            Type type = new TypeToken<Map<String, List<SettingButtons>>>() {}.getType();
            Map<String, List<SettingButtons>> loaded = GSON.fromJson(reader, type);
            reader.close();

            if (loaded != null) {
                featureSettings.clear();
                featureSettings.putAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Check if features is enabled in said feature type (section like Mining, Rift, etc)
    public static boolean isFeatureEnabled(String featureType, String featureName) {
        List<SettingButtons> toggles = featureSettings.get(featureType);
        if (toggles != null) {
            for (SettingButtons toggle : toggles) {
                if(featureName.equalsIgnoreCase(toggle.getLabel())) {
                    return toggle.getValue();
                }
            }
        }
        return false;
    }
}
