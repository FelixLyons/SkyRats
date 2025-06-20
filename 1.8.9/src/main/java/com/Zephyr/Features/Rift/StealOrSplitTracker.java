package com.Zephyr.Features.Rift;

import com.Zephyr.Core.Features.TimerTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class StealOrSplitTracker {
    private static boolean isActive = false;
    private boolean msgSent = false;

    // Check for Split or Steal start
    public static void check(String msg) {
        if(msg.startsWith("ROUND 1:") && msg.endsWith("!")) {
            isActive = true;
        }
    }

    // Sync time if user right click while it's already on cooldown
    public static void checkAlrCD(String msg) {
        if(msg.startsWith("SPLIT! You need to wait")) {
            int totalSec = parseCooldownTime(msg);
            if(TimerTracker.isCooldownActive("Split_Or_Steal")) {
                TimerTracker.clearCooldown("Split_Or_Steal");
                TimerTracker.startCooldown("Split_Or_Steal", totalSec);
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

        // Start Split or Steal time if not active
        if(isActive) {
            if(!TimerTracker.isCooldownActive("Split_Or_Steal")) {
                TimerTracker.startCooldown("Split_Or_Steal", 7200);
                msgSent = false;
            }
        } else if(!msgSent && TimerTracker.getCooldownTimeLeft("Split_Or_Steal") == 0) {
            isActive = false;
            TimerTracker.clearCooldown("Split_Or_Steal");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + "" +
                    EnumChatFormatting.RED + "[SR] " + EnumChatFormatting.RESET + "" + EnumChatFormatting.YELLOW +
                    "Split or Steal is available!"));
            msgSent = true;
        }

        TimerTracker.save();
    }
}