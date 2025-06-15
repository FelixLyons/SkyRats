package com.SkyRats.Features.Rift;

import com.SkyRats.Core.Features.TimerTracker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class StealOrSplitTracker {
    private static boolean isActive = false;

    public static void check(String msg) {
        if(msg.startsWith("Your opponent earned") && msg.endsWith("!")) {
            isActive = true;
        }
    }

    public static void checkAlrCD(String msg) {
        if(msg.startsWith("SPLIT! You need to wait")) {
            int totalSec = parseCooldownTime(msg);
            if(TimerTracker.isCooldownActive("SplitOrSteal")) {
                TimerTracker.clearCooldown("SplitOrSteal");
                TimerTracker.startCooldown("SplitOrSteal", totalSec);
            }
            isActive = true;
        }
    }

    // Parse the string of the cooldown given ingame into an integer (seconds)
    public static int parseCooldownTime(String msg) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        // Split string and remove format and get the hour
        if (msg.contains("h")) {
            String[] split = msg.split("h");
            hours = Integer.parseInt(split[0].replaceAll("[^0-9]", ""));
            msg = split[1];
        }

        // Split string and remove format and get the minutes
        if (msg.contains("m")) {
            String[] split = msg.split("m");
            minutes = Integer.parseInt(split[0].replaceAll("[^0-9]", ""));
            msg = split[1];
        }

        // Split string and remove format and get the seconds
        if (msg.contains("s")) {
            seconds = Integer.parseInt(msg.split("s")[0].replaceAll("[^0-9]", ""));
        }

        // Return and convert everything in seconds
        return hours * 3600 + minutes * 60 + seconds;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

        if(isActive) {
            if(!TimerTracker.isCooldownActive("SplitOrSteal")) {
                TimerTracker.startCooldown("SplitOrSteal", 7200);
            }
        } else {
            return;
        }

        if(TimerTracker.getCooldownTimeLeft("SplitOrSteal") == 0) {
            isActive = false;
            TimerTracker.clearCooldown("SplitOrSteal");
        }

        TimerTracker.save();
    }
}
