package com.skyrats.Backend.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.skyrats.Commands.SRCommand;
import net.minecraft.server.command.ServerCommandSource;

// Register command onto client
public class CommandsRegister {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        SRCommand.register(dispatcher);
    }
}
