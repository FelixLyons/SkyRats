package com.Zephyr.Core.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandList {
    private static final Map<String, CommandExecution> commandMap = new HashMap<String, CommandExecution>();

    public static void register(String keyword, CommandExecution command) {
        commandMap.put(keyword.toLowerCase(), command);
    }

    public static CommandExecution getCommand(String keyword) {
        return commandMap.get(keyword.toLowerCase());
    }

    public static boolean hasCommand(String keyword) {
        return commandMap.containsKey(keyword.toLowerCase());
    }

    public static Set<String> getAllCommandKeys() {
        return commandMap.keySet();
    }

}
