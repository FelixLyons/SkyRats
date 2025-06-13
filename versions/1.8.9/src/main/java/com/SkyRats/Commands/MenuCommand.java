package com.SkyRats.Commands;

import com.SkyRats.Core.Commands.CommandExecution;
import com.SkyRats.Core.GUI.GuiOpener;
import com.SkyRats.GUI.HomeGUI;

public class MenuCommand implements CommandExecution {
    @Override
    public void execute() {
        GuiOpener.queueGui(new HomeGUI());
    }
}
