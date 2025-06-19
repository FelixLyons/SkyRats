package com.SkyRats.Core.Commands;

import com.SkyRats.Commands.EditorCommand;
import com.SkyRats.Commands.MenuCommand;
import com.SkyRats.Commands.ShaftCommand;
import com.SkyRats.Commands.SplitOrStealTime;
import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.GUI.EditGUI;

//Registers all custom commands.
public class CommandRegisterAll {
    private final MineshaftTracker mineshaftTracker;
    private final EditGUI editGUI;

    public CommandRegisterAll(MineshaftTracker mineshaftTracker, EditGUI editGUI) {
        this.mineshaftTracker = mineshaftTracker;
        this.editGUI = editGUI;
    }

    public void execute() {
        CommandList.register("sr", new MenuCommand(editGUI));
        CommandList.register("sr shafts", new ShaftCommand(mineshaftTracker));
        CommandList.register("sr sos", new SplitOrStealTime());
        CommandList.register("sr gui", new EditorCommand(editGUI));
    }
}
