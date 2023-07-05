package com.readutf.server.servers;

import com.readutf.quickmatch.shared.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerManager {

    private final Map<UUID, Server> idToServer;
    private final Map<String, Server> ipToServer;

    public ServerManager() {
        idToServer = new HashMap<>();
        ipToServer = new HashMap<>();
    }

    public void registerServer(Server server) {
        Server existingByIp = ipToServer.get(server.getCombinedAddress());
        if (existingByIp != null) {
            unregisterServer(existingByIp.getServerId());
        }

        idToServer.put(server.getServerId(), server);
        ipToServer.put(server.getCombinedAddress(), server);
    }

    public boolean unregisterServer(UUID serverId) {
        Server server = idToServer.get(serverId);
        if (server == null) return false;
        idToServer.remove(serverId);
        ipToServer.remove(server.getCombinedAddress());
        return true;
    }

}
