package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class LocationCommand implements CommandExecution {
    @Override
    public void execute() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PlayerLocationChecker.getLocation().toString()));
    }
}
