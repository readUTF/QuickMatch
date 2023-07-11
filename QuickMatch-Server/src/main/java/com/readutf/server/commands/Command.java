package com.readutf.server.commands;

import lombok.Getter;

public abstract class Command {

    private final @Getter String name;
    private final @Getter int minArgs;
    private final @Getter String usage;

    public Command(String name, int minArgs, String usage) {
        this.name = name;
        this.minArgs = minArgs;
        this.usage = usage;
    }

    public abstract void onCommand(String[] args);

}
