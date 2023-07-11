package com.readutf.proxy.balancing;

import com.readutf.quickmatch.shared.Server;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.List;

public interface BalanceMode {

    Server findServer(List<Server> serverList);

}
