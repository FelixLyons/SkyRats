package com.SkyRats.Core.Features;

import java.io.File;
import java.util.Arrays;

public class FeatureSettings {
    private static final String SETTINGS_FILE_PATH = "SkyRats/settings.json";
    public static void run() {
        File settingsFile = new File(SETTINGS_FILE_PATH);
        if(settingsFile.exists()) {
            //Load save settings
            SettingsManager.load();
        }else {
            SettingsManager.register("Mining", Arrays.asList(
                    new SettingButtons("Mineshaft Tracker", false)
            ));
            SettingsManager.register("Alerts", Arrays.asList(
                    new SettingButtons("Mining Ability Cooldown Notification", false)
            ));
            //Save
            SettingsManager.save();
        }
    }
}
