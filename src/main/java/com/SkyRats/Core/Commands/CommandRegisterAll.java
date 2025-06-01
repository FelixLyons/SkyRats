package com.SkyRats.Core.Commands;

import com.SkyRats.Commands.MenuCommand;

//Registers all custom commands.
public class CommandRegisterAll {
    public static void execute() {
        CommandList.register("sr", new MenuCommand());
    }
}
