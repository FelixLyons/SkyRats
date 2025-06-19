package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.GUI.EditGUI;
import com.SkyRats.GUI.HomeGUI;

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
