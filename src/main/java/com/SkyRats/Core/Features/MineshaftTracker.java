package com.SkyRats.Core.Features;

import java.util.EnumMap;
import java.util.Map;

public class MineshaftTracker {
    private final Map<ShaftTypes, Integer> shaftCounts = new EnumMap<>(ShaftTypes.class);

    public MineshaftTracker() {
        for (ShaftTypes type : ShaftTypes.values()) {
            shaftCounts.put(type, 0);
        }
    }

    public void incrementShaft(ShaftTypes type) {
        shaftCounts.put(type, shaftCounts.get(type) + 1);
    }

    public int getCount(ShaftTypes type) {
        return shaftCounts.get(type);
    }

    public int getTotalShafts() {
        return shaftCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
}
