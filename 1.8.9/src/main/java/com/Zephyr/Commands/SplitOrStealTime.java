package com.Zephyr.Commands;

import com.Zephyr.Core.Commands.CommandExecution;
import com.Zephyr.Core.Features.TimerTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class SplitOrStealTime implements CommandExecution {
    @Override
    public void execute() {
        int totalTime = TimerTracker.getCooldownTimeLeft("Split_Or_Steal");
        int hours = totalTime / 3600;
        int minutes = (totalTime % 3600) / 60;
        int seconds = totalTime % 60;

        if(hours == 0 && minutes == 0 && seconds == 0) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + "" +
                    EnumChatFormatting.RED + "[SR] " + EnumChatFormatting.RESET + "" + EnumChatFormatting.YELLOW +
                    "Split or Steal: " + EnumChatFormatting.GREEN + "Available"));
        }else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + "" +
                    EnumChatFormatting.RED + "[SR] " + EnumChatFormatting.RESET + "" + EnumChatFormatting.YELLOW +
                    "Split or Steal: " + hours + "h " + minutes + "m " + seconds + "s"));
        }
    }
}