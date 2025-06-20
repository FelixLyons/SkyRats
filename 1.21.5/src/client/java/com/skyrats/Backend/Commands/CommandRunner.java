package com.skyrats.Backend.Commands;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

// Runs the command
public class CommandRunner implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandsRegister.registerCommands(dispatcher);
        });
    }
}
