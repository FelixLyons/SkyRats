package com.SkyRats.Commands;

//Registers all custom commands.
public class CommandRegisterAll {
    public static void execute() {
        CommandList.register("sr", new MenuCommand());
    }
}
