package com.readutf.proxy.subscriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.readutf.hermes.pipline.listeners.ParcelListener;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.proxy.QuickMatchProxy;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.proxy.utils.Logger;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.PlayerMessage;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.ServerPing;
import com.readutf.quickmatch.shared.serializers.ServerPingSerializer;
import com.readutf.quickmatch.shared.serializers.ServerIdSerializer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.*;

public class Subscriber {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final QuickMatchProxy quickMatchProxy;

    public Subscriber(QuickMatchProxy quickMatchProxy) {
        this.quickMatchProxy = quickMatchProxy;
    }

    @ParcelListener("API_RESTART")
    public void onRestart(ParcelWrapper parcelWrapper) {
        quickMatchProxy.getServerManager().restart();
        quickMatchProxy.getProxyManager().restart();
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
                proxyServer.getServer("server- " + gameData.getServer().getServerId()).ifPresent(registeredServer -> {
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

    @ParcelListener("PLAYER_MESSAGE")
    public void onPlayerMessage(ParcelWrapper parcelWrapper) {
        PlayerMessage playerMessage = parcelWrapper.get(new TypeReference<>() {});

        if (playerMessage.getPermission() != null) {
            for (Player allPlayer : quickMatchProxy.getProxyServer().getAllPlayers()) {
                if (allPlayer.hasPermission(playerMessage.getPermission())) {
                    for (String message : playerMessage.getMessages()) {
                        allPlayer.sendMessage(ColorUtils.colorize(message));
                    }
                }
            }
        } else {
            for (UUID player : playerMessage.getPlayerIds()) {
                quickMatchProxy.getProxyServer().getPlayer(player).ifPresent(player1 -> {
                    for (String message : playerMessage.getMessages()) {
                        player1.sendMessage(ColorUtils.colorize(message));
                    }
                });
            }
        }


    }

    @ParcelListener("SERVER_UNREGISTER")
    public void onServerUnregister(ParcelWrapper parcelWrapper) {
        int serverId;
        try {
            serverId = parcelWrapper.get(new ServerIdSerializer());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        quickMatchProxy.getServerManager().unregisterServer(serverId);
    }

}
