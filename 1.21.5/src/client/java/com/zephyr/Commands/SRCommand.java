package com.zephyr.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.zephyr.Backend.GUIs.GuiOpener;
import com.zephyr.GUIs.HomeGUI;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

// Opens SR Menu
public class SRCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("sr")
                .executes(context -> {
                    GuiOpener.queueGui(new HomeGUI());
                    return 1;
                }));
    }
}
