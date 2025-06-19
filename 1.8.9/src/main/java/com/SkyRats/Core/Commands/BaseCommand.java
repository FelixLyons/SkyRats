package com.SkyRats.Core.Commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseCommand extends GuiChat {
    // Stores current matching completions while tabbing
    private List<String> tabCompletions = new ArrayList<String>();
    // Tracks which completion is currently selected
    private int completionIndex = -1;

    //Constructor for normal chat (T default keybind)
    public BaseCommand() {
        super();
    }

    //Constructor for command chat (/ default keybind). Makes it so when command keybind is pressed, it will start with "/" instead of clear.
    public BaseCommand(String defaultInputText) {
        super(defaultInputText);
    }

    //Checks player chat message for command
    @Override
    public void sendChatMessage(String message) {
        //Intercept message from being sent and execute command if valid.
        if(message.startsWith("/")) {
            String commandKey = message.substring(1);
            CommandExecution command = CommandList.getCommand(commandKey);
            boolean commandExists = CommandList.hasCommand(commandKey);
            if(commandExists) {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
                command.execute();
                return;
            }
        }
        //Send chat message like normal
        super.sendChatMessage(message);
    }

    // Handle keyboard input (TAB key used for command suggestions)
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_TAB) {
            if (!handleTabCompletion()) {
                // Fallback to Minecraft's original autocomplete if no custom matches
                super.keyTyped(typedChar, keyCode);
            }
            return;
        }

        // Reset tab completions if any other key is pressed
        tabCompletions.clear();
        completionIndex = -1;

        super.keyTyped(typedChar, keyCode);
    }

    // Finds all matching commands based on user input parts
    private List<String> getMatchingCommands(String[] parts) {
        List<String> matches = new ArrayList<String>();

        // If no input parts, return empty list
        if (parts.length == 0) {
            return matches;
        }

        // The base command prefix, "sr"
        String basePrefix = parts[0].toLowerCase();

        // Iterate all registered commands
        for (String fullCmd : CommandList.getAllCommandKeys()) {
            // Split full command by spaces into parts
            String[] cmdParts = fullCmd.split(" ");

            // Skip commands that don't start with the base prefix
            if (cmdParts.length == 0 || !cmdParts[0].equalsIgnoreCase(basePrefix)) {
                continue;
            }

            // Check all typed parts except the last to ensure exact match
            boolean match = true;
            for (int i = 1; i < parts.length - 1; i++) {
                if (i >= cmdParts.length || !cmdParts[i].equalsIgnoreCase(parts[i])) {
                    match = false;
                    break;
                }
            }
            if (!match) continue;

            if (parts.length > 1) {
                // If user has typed more than base command, check if last typed part matches start of next command part
                if (cmdParts.length < parts.length) continue; // command too short to match

                String lastTyped = parts[parts.length - 1].toLowerCase();
                String nextCmdPart = cmdParts[parts.length - 1].toLowerCase();

                if (nextCmdPart.startsWith(lastTyped)) {
                    // Add next part as possible completion suggestion
                    matches.add(cmdParts[parts.length - 1]);
                }
            } else {
                // User typed only the base command ("sr")
                if (cmdParts.length > 1) {
                    matches.add(cmdParts[1]);
                }
            }
        }

        return matches;
    }

    // Handles TAB completion logic triggered by TAB key press
    private boolean handleTabCompletion() {
        String currentText = this.inputField.getText();

        // Only proceed if input starts with slash (command)
        if (!currentText.startsWith("/")) {
            return false;
        }

        // Remove leading slash and split input by spaces into parts
        String inputWithoutSlash = currentText.substring(1);
        String[] parts = inputWithoutSlash.split(" ");

        // On first TAB press, gather possible completions
        if (tabCompletions.isEmpty()) {
            tabCompletions = getMatchingCommands(parts);

            // If no matches, do nothing
            if (tabCompletions.isEmpty()) {
                return false;
            }

            // If multiple matches, show them in chat for user info
            if (tabCompletions.size() > 1) {
                StringBuilder display = new StringBuilder("");
                for (int i = 0; i < tabCompletions.size(); i++) {
                    display.append(tabCompletions.get(i));
                    if (i < tabCompletions.size() - 1) {
                        display.append(", ");
                    }
                }

                // Print suggestions to the in-game chat GUI
                Minecraft.getMinecraft().ingameGUI.getChatGUI()
                        .printChatMessage(new ChatComponentText(display.toString()));
            }

            // Start cycling through completions from first match
            completionIndex = 0;
        } else {
            // TAB presses cycle through the completion list
            completionIndex = (completionIndex + 1) % tabCompletions.size();
        }

        // Extract base command (e.g. "sr")
        String baseCommand = parts[0];

        // Collect any arguments typed before the last argument
        String prefix = "";
        if (parts.length > 1) {
            prefix = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length - 1));
            if (!prefix.isEmpty()) {
                prefix += " ";
            }
        }

        // Get the currently selected completion suggestion
        String completion = tabCompletions.get(completionIndex);

        // Construct the new input text:
        // "/" + base command + (arguments before last) + new completion suggestion
        String newText;
        if (completion.isEmpty()) {
            // If completion is empty, just show the base command
            newText = "/" + baseCommand;
        } else {
            newText = "/" + baseCommand + " " + prefix + completion;
        }

        // Set the text in the input field and move text cursor to the end
        this.inputField.setText(newText.trim());
        this.inputField.setCursorPositionEnd();
        return true;
    }


    //When GUI opens, it does not pause the game.
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}