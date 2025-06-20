package com.Zephyr.GUI.HUD;

import com.Zephyr.Core.GUI.MovableUIs;

import java.util.ArrayList;
import java.util.List;

// Manages all HUDs
public class HUDManager {
    private static final List<MovableUIs> HUDS = new ArrayList<MovableUIs>();

    // Register active HUDs
    public static void register(MovableUIs hud) {
        HUDS.add(hud);
    }

    // Returns all active HUDs
    public static List<MovableUIs> getAllHUDs() {
        return new ArrayList<MovableUIs>(HUDS);
    }
}
