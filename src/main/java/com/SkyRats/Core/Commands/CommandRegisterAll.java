package com.SkyRats.Core.Commands;

import com.SkyRats.Commands.LocationCommand;
import com.SkyRats.Commands.MenuCommand;
import com.SkyRats.Commands.ShaftCommand;
import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;

//Registers all custom commands.
public class CommandRegisterAll {
    private MineshaftTracker mineshaftTracker;

    public CommandRegisterAll(MineshaftTracker mineshaftTracker) {
        this.mineshaftTracker = mineshaftTracker;
    }

    public void execute() {
        CommandList.register("sr", new MenuCommand());
        CommandList.register("sr shafts", new ShaftCommand(mineshaftTracker));
        CommandList.register("sr wmi", new LocationCommand());
    }
}
