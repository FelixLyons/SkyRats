package com.SkyRats.Core.GUI;

import com.SkyRats.GUI.HUD.HUDManager;
import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HUDSettings {
    @Expose
    private String id;

    @Expose private int x, y;

    public HUDSettings(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static void applyHUDPositions(File file) {
        Map<String, List<HUDSettings>> savedPositions = SettingsManager.load(file, HUDSettings.class);

        List<MovableUIs> huds = HUDManager.getAllHUDs();

        for (Map.Entry<String, List<HUDSettings>> entry : savedPositions.entrySet()) {
            String hudName = entry.getKey();
            List<HUDSettings> positions = entry.getValue();

            if (positions == null || positions.isEmpty()) continue;

            HUDSettings pos = positions.get(0);  // Get first (and only) element

            for (MovableUIs hud : huds) {
                if (hud.getName().equals(hudName)) {
                    hud.setPosition(pos.getX(), pos.getY());
                    break;
                }
            }
        }
    }

    public static void load() {
        File saveFile = new File(Minecraft.getMinecraft().mcDataDir, "config/SkyRats/hud_config.json");
        Map<String, List<HUDSettings>> savedPositions = SettingsManager.load(saveFile, HUDSettings.class);

        for (MovableUIs ui : HUDManager.getAllHUDs()) {
            List<HUDSettings> settingsList = savedPositions.get(ui.getName());
            if (settingsList != null && !settingsList.isEmpty()) {
                HUDSettings settings = settingsList.get(0);
                ui.setPosition(settings.getX(), settings.getY());
            }
        }
    }
}
