package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.GUI.EditGUI;
import com.SkyRats.GUI.HomeGUI;

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
