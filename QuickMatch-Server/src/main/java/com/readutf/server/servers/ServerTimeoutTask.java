package com.readutf.server.servers;

import com.readutf.quickmatch.shared.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class ServerTimeoutTask extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(ServerTimeoutTask.class);

    private final ServerManager serverManager;

    public ServerTimeoutTask(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        for (Server server : serverManager.getServers()) {
            if (System.currentTimeMillis() - server.getLastPing() > 10000) {
                logger.warn("Server " + server.getServerId() + " has timed out.");
                serverManager.unregisterServer(server.getServerId());
            }
        }
    }
}
