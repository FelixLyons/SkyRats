package com.SkyRats.Core.Commands;

import net.minecraft.client.gui.GuiChat;

public class BaseCommand extends GuiChat {
    //Constructor for normal chat (T default keybind)
    public BaseCommand() {
        super();
    }

    //Constructor for command chat (/ default keybind). Makes it so when command keybind is pressed, it will start with "/" instead of clear.
    public BaseCommand(String defaultInputText) {
        super(defaultInputText);
    }

    @Override
    public void sendChatMessage(String message) {
        //Intercept message from being sent and execute command if valid.
        if(message.startsWith("/")) {
            String commandKey = message.substring(1).split(" ")[0];
            CommandExecution command = CommandList.getCommand(commandKey);
            boolean commandExists = CommandList.hasCommand(commandKey);
            if(commandExists) {
                command.execute();
                return;
            }
        }
        //Send chat message like normal
        super.sendChatMessage(message);
    }
}
