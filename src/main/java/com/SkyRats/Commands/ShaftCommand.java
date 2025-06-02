package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.Features.MineshaftTracker;
import com.SkyRats.Core.Features.ShaftTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

//Shows all mineshafts that you have gotten
public class ShaftCommand implements CommandExecution {
    private final MineshaftTracker mineshaftTracker;

    public ShaftCommand(MineshaftTracker mineshaftTracker) {
        this.mineshaftTracker = mineshaftTracker;
    }

    @Override
    public void execute() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Shaft Data:"));

        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<Map.Entry<ShaftTypes, Integer>>();

        for (ShaftTypes type : ShaftTypes.values()) {
            int count = mineshaftTracker.getCount(type);
            if (count > 0) {
                entries.add(new AbstractMap.SimpleEntry<ShaftTypes, Integer>(type, count));
            }
        }

        //Sort from highest to lowest
        Collections.sort(entries, new Comparator<Map.Entry<ShaftTypes, Integer>>() {
            @Override
            public int compare(Map.Entry<ShaftTypes, Integer> a, Map.Entry<ShaftTypes, Integer> b) {
                return Integer.compare(b.getValue(), a.getValue());
            }
        });

        //Display results
        for (Map.Entry<ShaftTypes, Integer> entry : entries) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText( EnumChatFormatting.AQUA + entry.getKey().name() + ": " + EnumChatFormatting.YELLOW + entry.getValue()));
        }

        //Total shafts
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Total: " + EnumChatFormatting.GREEN + mineshaftTracker.getTotalShafts()));
    }
}
