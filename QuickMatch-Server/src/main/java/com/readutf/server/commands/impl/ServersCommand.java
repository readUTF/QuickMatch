package com.readutf.server.commands.impl;

import com.readutf.quickmatch.shared.Server;
import com.readutf.server.commands.Command;
import com.readutf.server.commands.CommandManager;
import com.readutf.server.servers.ServerManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServersCommand extends Command {

    private final ServerManager commandManager;

    public ServersCommand(ServerManager commandManager) {
        super("servers", 0, "");
        this.commandManager = commandManager;
    }


    @Override
    public void onCommand(String[] args) {
        List<Server> servers = commandManager.getServers();
        if(servers.isEmpty()) {
            System.out.println("No servers found!");
            return;
        }
        System.out.println("Servers: " + servers.stream().map(Server::getShortName).collect(Collectors.joining(", ")));
    }
}
