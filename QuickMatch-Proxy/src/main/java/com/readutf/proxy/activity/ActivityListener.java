package com.readutf.proxy.activity;

import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;

@AllArgsConstructor
public class ActivityListener {

    private final Supplier<ProxyInfo> serverSupplier;
    private final LiveProfileManager liveProfileManager;

    @Subscribe
    public void onServerLeave(DisconnectEvent e) {
        Player player = e.getPlayer();

        liveProfileManager.save(new LiveProfile(
                player.getUniqueId(),
                player.getUsername(),
                e.getPlayer().getCurrentServer().map(serverConnection -> serverConnection.getServerInfo().getName()).orElse(""),
                Optional.ofNullable(serverSupplier.get()).map(ProxyInfo::getName).orElse(""),
                false,
                System.currentTimeMillis(),
                ""
        ));

    }

    @Subscribe
    public void onServerJoin(ServerConnectedEvent e) {
        if(e.getPreviousServer().isEmpty()) {
            Player player = e.getPlayer();
            liveProfileManager.save(new LiveProfile(
                    player.getUniqueId(),
                    player.getUsername(),
                    e.getServer().getServerInfo().getName(),
                    e.getPlayer().getCurrentServer().map(serverConnection -> serverConnection.getServerInfo().getName()).orElse(""),
                    true,
                    System.currentTimeMillis(),
                    ""
            ));
        }
    }

}
