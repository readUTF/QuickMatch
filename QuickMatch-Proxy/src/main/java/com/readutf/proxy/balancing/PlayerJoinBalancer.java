package com.readutf.proxy.balancing;

import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.proxy.utils.Logger;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.List;
import java.util.Optional;

public class PlayerJoinBalancer {

    private final ProxyServer proxyServer;
    private final ServerManager serverManager;

    public PlayerJoinBalancer(ProxyServer proxyServer, ServerManager serverManager) {
        this.serverManager = serverManager;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onJoin(PlayerChooseInitialServerEvent e) {
        Optional<RegisteredServer> server = e.getInitialServer();
        if (server.isPresent() && !server.get().getServerInfo().getName().equalsIgnoreCase("lobby")) return;

        String hubType = "hub_main";// TODO: 06/07/2023 Collect users last hub from db
        Logger.getLOGIN().info("Finding hub with id %s for %s".formatted(hubType, e.getPlayer().getGameProfile().getName()));

        List<RegisteredServer> mainHub = serverManager.getServersByType(hubType);
        if (mainHub.isEmpty()) {
            Logger.getLOGIN().error("No servers found for hub type %s".formatted(hubType));
            e.getPlayer().disconnect(ColorUtils.colorize("&cNo servers found"));
            return;
        } else {
            RegisteredServer registeredServer = mainHub.get(0);
            Logger.getLOGIN().info("Found server %s for %s".formatted(registeredServer.getServerInfo().getName(), e.getPlayer().getGameProfile().getName()));
            e.setInitialServer(registeredServer);
        }

    }

}
