package com.skyrats.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.skyrats.Backend.GUIs.GuiOpener;
import com.skyrats.GUIs.HomeGUI;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

// Opens SR Menu
public class SRCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sr")
                .executes(context -> {
                    GuiOpener.queueGui(new HomeGUI());
                    return 1;
                }));
    }
}
