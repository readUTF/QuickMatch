package com.readutf.server.servers;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.ServerPing;
import com.readutf.quickmatch.shared.serializers.ServerIdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServerManager {

    private final Logger logger = LoggerFactory.getLogger(ServerManager.class);

    private final Hermes hermes;
    private final Map<Integer, Server> idToServer;
    private final Map<String, Server> ipToServer;

    public ServerManager(Hermes hermes, Timer timer) {
        this.hermes = hermes;
        idToServer = new HashMap<>();
        ipToServer = new HashMap<>();
        timer.scheduleAtFixedRate(new ServerTimeoutTask(this), 0, 5000);
    }

    public void registerServer(Server server) {
        Server existingByIp = ipToServer.get(server.getCombinedAddress());
        if (existingByIp != null) {
            unregisterServer(existingByIp.getServerId());
        }

        idToServer.put(server.getServerId(), server);
        ipToServer.put(server.getCombinedAddress(), server);
        hermes.sendParcel("SERVER_REGISTER", server);
        logger.info("Registered server " + server.getServerId() + " (" + server.getCombinedAddress() + ")");
    }

    public List<Server> getServers() {
        return new ArrayList<>(idToServer.values());
    }

    public boolean unregisterServer(int serverId) {
        Server server = idToServer.get(serverId);
        if (server == null) return false;
        idToServer.remove(serverId);
        ipToServer.remove(server.getCombinedAddress());
        hermes.sendParcel("SERVER_UNREGISTER", serverId, new ServerIdSerializer());
        return true;
    }

    public void handlePing(ServerPing serverPing) {
        Server server = idToServer.get(serverPing.getServerId());
        if (server == null) return;
        server.setLastPing(System.currentTimeMillis());
        server.setPlayerCount(serverPing.getPlayerCount());
    }
}
