package com.readutf.proxy.server;

import com.readutf.proxy.utils.ColorUtils;
import com.readutf.proxy.utils.Logger;
import com.readutf.proxy.utils.MessageUtils;
import com.readutf.quickmatch.shared.*;
import com.readutf.quickmatch.shared.utils.CachedValue;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.util.*;

public class ServerManager {

    private final ProxyServer proxyServer;
    private final ServerService serverService;
    private final CachedValue<List<Server>> servers;
    private @Getter final Map<String, List<RegisteredServer>> typeToServers = new HashMap<>();
    private final Map<RegisteredServer, Server> registeredServerToServer;
    private final Map<Server, RegisteredServer> serverToRegisteredServer;
    private final Map<UUID, Server> idToServer;

    public ServerManager(ProxyServer proxyServer, Retrofit retrofit) {
        this.proxyServer = proxyServer;
        this.serverService = retrofit.create(ServerService.class);
        this.servers = new CachedValue<>(() -> RequestHelper.get(serverService.getServers()), 1000 * 60 * 5);
        this.idToServer = new HashMap<>();
        this.registeredServerToServer = new HashMap<>();
        this.serverToRegisteredServer = new HashMap<>();

        for (Server server : getServers(true)) {
            registerServer(server);
        }
    }

    public void restart() {
        List<Server> servers = getServers(true);
        for (RegisteredServer allServer : proxyServer.getAllServers()) {
            UUID serverId = UUID.fromString(allServer.getServerInfo().getName());
            boolean noneMatch = true;
            for (Server server : new ArrayList<>(servers)) {
                if (server.getServerId().equals(serverId)) {
                    noneMatch = false;
                    servers.remove(server);
                    break;
                }
            }
            if (noneMatch) {
                unregisterServer(serverId);
            }
        }
        for (Server server : servers) registerServer(server);
    }

    public List<Server> getServers(boolean ignoreCache) {
        return servers.getValue(ignoreCache);
    }

    public List<RegisteredServer> getServersByType(String type) {
        return typeToServers.getOrDefault(type.toLowerCase(), Collections.emptyList());
    }

    public Server getServerById(UUID serverId) {
        return idToServer.get(serverId);
    }

    public void registerServer(Server server) {
        ServerInfo serverInfo = new ServerInfo(server.getServerId().toString(), new InetSocketAddress(server.getAddress(), server.getPort()));
        RegisteredServer registeredServer = proxyServer.registerServer(serverInfo);
        typeToServers.compute(server.getServerType().toLowerCase(), (s, registeredServers) -> {
            if (registeredServers == null) registeredServers = new ArrayList<>();
            registeredServers.add(registeredServer);
            return registeredServers;
        });
        idToServer.put(server.getServerId(), server);
        registeredServerToServer.put(registeredServer, server);
        serverToRegisteredServer.put(server, registeredServer);

        MessageUtils.sendPermissionMessage(
                "&9&lGM &8» &7A new &b%s &7server has registered with id &b%s".formatted(server.getServerType(), server.getShortName()),
                "quickmatch.server.notify"
        );

        Logger.getSERVER().info("Registered server %s (%s:%s)".formatted(server.getShortName(), server.getAddress(), server.getPort()));
    }

    public Server getServer(RegisteredServer registeredServer) {
        return registeredServerToServer.get(registeredServer);
    }

    public void handlePing(ServerPing serverPing) {

        Server server = idToServer.get(serverPing.getServerId());
        server.setLastPing(System.currentTimeMillis());
        server.setPlayerCount(serverPing.getPlayerCount());
        server.setTps(serverPing.getTps());
    }

    public void unregisterServer(UUID uuid) {
        proxyServer.getServer(uuid.toString()).ifPresent(registeredServer ->
                proxyServer.unregisterServer(registeredServer.getServerInfo())
        );
        Server server = idToServer.remove(uuid);
        RegisteredServer registeredServer = serverToRegisteredServer.get(server);
        registeredServerToServer.remove(registeredServer);
        serverToRegisteredServer.remove(server);
        typeToServers.computeIfPresent(server.getServerType().toLowerCase(), (s, registeredServers) -> {
            if (registeredServers.remove(registeredServer)) {
                System.out.println("removed from a list");
            }
            return registeredServers;
        });

        if(System.currentTimeMillis() - server.getLastPing() > 10000) {
            MessageUtils.sendPermissionMessage(
                    "&9&lGM &8» &7A server with id &b%s &7has &ctimed out".formatted(server.getShortName()),
                    "quickmatch.server.notify"
            );
        } else {
            MessageUtils.sendPermissionMessage(
                    "&9&lGM &8» &7A server with id &b%s &7has gone offline".formatted(server.getShortName()),
                    "quickmatch.server.notify"
            );
        }
        Logger.getSERVER().info("Un-Registered server %s".formatted(uuid));
    }
}
