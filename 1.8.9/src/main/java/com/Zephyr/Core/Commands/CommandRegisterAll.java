package com.Zephyr.Core.Commands;

import com.Zephyr.Commands.EditorCommand;
import com.Zephyr.Commands.MenuCommand;
import com.Zephyr.Commands.ShaftCommand;
import com.Zephyr.Commands.SplitOrStealTime;
import com.Zephyr.Core.Features.Mineshafts.MineshaftTracker;
import com.Zephyr.GUI.EditGUI;

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
