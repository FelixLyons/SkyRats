package com.Zephyr.Commands;

import com.Zephyr.Core.Commands.CommandExecution;
import com.Zephyr.Core.GUI.GuiOpener;
import com.Zephyr.GUI.EditGUI;
import com.Zephyr.GUI.HomeGUI;

public class MenuCommand implements CommandExecution {
    private final EditGUI editGUI;

    public MenuCommand(EditGUI editGUI) {
        this.editGUI = editGUI;
    }

    @Override
    public void execute() {
        GuiOpener.queueGui(new HomeGUI(editGUI));
    }
}
