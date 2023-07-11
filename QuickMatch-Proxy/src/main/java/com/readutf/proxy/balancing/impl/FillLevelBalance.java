package com.readutf.proxy.balancing.impl;

import com.readutf.proxy.balancing.BalanceMode;
import com.readutf.quickmatch.shared.Server;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Comparator;
import java.util.List;

public class FillLevelBalance implements BalanceMode {

    private final int baseLevel;

    public FillLevelBalance(int baseLevel) {
        this.baseLevel = baseLevel;
    }

    @Override
    public Server findServer(List<Server> serverList) {
        for (Server server : serverList) {
            if(server.getPlayerCount() < baseLevel) return server;
        }
        return serverList.stream().min(Comparator.comparingInt(Server::getPlayerCount)).orElse(null);
    }
}
