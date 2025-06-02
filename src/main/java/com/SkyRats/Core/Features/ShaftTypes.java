package com.SkyRats.Core.Features;

import net.minecraft.util.EnumChatFormatting;

import java.util.EnumSet;

//All ShaftTypes and their respected color
public enum ShaftTypes {
    JASPER(EnumChatFormatting.LIGHT_PURPLE),
    JADE(EnumChatFormatting.GREEN),
    AMETHYST(EnumChatFormatting.DARK_PURPLE),
    AMBER(EnumChatFormatting.GOLD),
    SAPPHIRE(EnumChatFormatting.AQUA),
    TOPAZ(EnumChatFormatting.YELLOW),
    RUBY(EnumChatFormatting.RED),
    OPAL(EnumChatFormatting.WHITE),
    AQUAMARINE(EnumChatFormatting.DARK_AQUA),
    PERIDOT(EnumChatFormatting.DARK_GREEN),
    ONYX(EnumChatFormatting.BLACK),
    CITRINE(EnumChatFormatting.DARK_RED),
    TUNGSTEN(EnumChatFormatting.DARK_GRAY),
    UMBER(EnumChatFormatting.DARK_RED),
    TITANIUM(EnumChatFormatting.GRAY),
    VANGUARD(EnumChatFormatting.YELLOW);

    private final String color;

    ShaftTypes(EnumChatFormatting color) {
        this.color = color.toString();
    }

    //Get the color for text coloring
    public String getColor() {
        return this.color;
    }
}
