package com.zephyr.Backend.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.zephyr.Commands.SRCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.server.command.ServerCommandSource;

// Register command onto client
public class CommandsRegister {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        SRCommand.register(dispatcher);
    }
}
