package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

// Chat utilities for client side chat messages
public class ChatUtils {
    // Clickable click messages that runs a command
    public static void sendClickableMessage(String text, EnumChatFormatting textColor, String clickableText, EnumChatFormatting clickableTextColor, String command, String hoverText) {
        // [SR] always BOLD
        ChatComponentText srPrefix = new ChatComponentText("[SR] ");
        srPrefix.setChatStyle(new ChatStyle().setColor(textColor).setBold(true));

        // Normal message text
        ChatComponentText message = new ChatComponentText(text);
        message.setChatStyle(new ChatStyle().setColor(textColor).setBold(false));

        ChatComponentText clickable = new ChatComponentText(clickableTextColor + "" + EnumChatFormatting.BOLD + clickableText);
        clickable.setChatStyle(new ChatStyle()
                .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ChatComponentText(EnumChatFormatting.GRAY + hoverText))));

        // Put all text together
        srPrefix.appendSibling(message);
        srPrefix.appendSibling(clickable);

        Minecraft.getMinecraft().thePlayer.addChatMessage(message);
    }
}
