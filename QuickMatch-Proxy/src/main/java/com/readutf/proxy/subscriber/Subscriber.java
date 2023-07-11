package com.readutf.proxy.subscriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.readutf.hermes.pipline.listeners.ParcelListener;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.proxy.QuickMatchProxy;
import com.readutf.proxy.utils.Logger;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.ServerPing;
import com.readutf.quickmatch.shared.serializers.ServerPingSerializer;
import com.readutf.quickmatch.shared.serializers.UUIDSerializer;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Subscriber {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final QuickMatchProxy quickMatchProxy;

    public Subscriber(QuickMatchProxy quickMatchProxy) {
        this.quickMatchProxy = quickMatchProxy;
    }

    @ParcelListener("API_RESTART")
    public void onRestart(ParcelWrapper parcelWrapper) {
        quickMatchProxy.getServerManager().restart();
    }

    @ParcelListener("SERVER_SWITCH")
    public void onServerSwitch(ParcelWrapper parcelWrapper) {
        Map<String, Object> data = parcelWrapper.get(new TypeReference<>() {});
        List<UUID> players = objectMapper.convertValue(data.get("players"), new TypeReference<>() {});
        GameData gameData = objectMapper.convertValue(data.get("server"), new TypeReference<>() {});

        Logger.getQUEUE().info("Players: " + players);
        Logger.getQUEUE().info("gameData: " + gameData);

        ProxyServer proxyServer = quickMatchProxy.getProxyServer();
        for (UUID playerId : players) {
            proxyServer.getPlayer(playerId).ifPresent(player -> {
                proxyServer.getServer(gameData.getServer().getServerId().toString()).ifPresent(registeredServer -> {
                    player.createConnectionRequest(registeredServer).fireAndForget();
                });
            });
        }

    }

    @ParcelListener("SERVER_REGISTER")
    public void onServerRegister(ParcelWrapper parcelWrapper) {
        Server server;
        try {
            server = parcelWrapper.get(Server.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        quickMatchProxy.getServerManager().registerServer(server);
    }

    @ParcelListener("SERVER_PING")
    public void onServerPing(ParcelWrapper parcelWrapper) {
        ServerPing serverPing;
        try {
            serverPing = parcelWrapper.get(new ServerPingSerializer());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        quickMatchProxy.getServerManager().handlePing(serverPing);
    }

    @ParcelListener("SERVER_UNREGISTER")
    public void onServerUnregister(ParcelWrapper parcelWrapper) {
        UUID uuid;
        try {
            uuid = parcelWrapper.get(new UUIDSerializer());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        quickMatchProxy.getServerManager().unregisterServer(uuid);
    }

}
