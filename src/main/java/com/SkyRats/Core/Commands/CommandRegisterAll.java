package com.SkyRats.Core.Commands;

import com.SkyRats.Commands.MenuCommand;
import com.SkyRats.Commands.ShaftCommand;
import com.SkyRats.Core.Features.MineshaftTracker;

//Registers all custom commands.
public class CommandRegisterAll {
    private MineshaftTracker mineshaftTracker;

    public CommandRegisterAll(MineshaftTracker mineshaftTracker) {
        this.mineshaftTracker = mineshaftTracker;
    }

    public void execute() {
        CommandList.register("sr", new MenuCommand());
        CommandList.register("sr shaft", new ShaftCommand(mineshaftTracker));
    }
}
