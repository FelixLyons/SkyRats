package com.Zephyr.Commands;

import com.Zephyr.Core.Commands.CommandExecution;
import com.Zephyr.Core.GUI.GuiOpener;
import com.Zephyr.GUI.EditGUI;

public class EditorCommand implements CommandExecution {
    private final EditGUI editGUI;

    public EditorCommand(EditGUI editGUI) {
        this.editGUI = editGUI;
    }

    @Override
    public void execute() {
        GuiOpener.queueGui(editGUI);
    }
}
