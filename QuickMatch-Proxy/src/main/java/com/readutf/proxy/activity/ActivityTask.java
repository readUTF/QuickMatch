package com.readutf.proxy.activity;

import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.TimerTask;
import java.util.function.Supplier;

@AllArgsConstructor
public class ActivityTask extends TimerTask {

    private final ProxyServer proxyServer;
    private final Supplier<ProxyInfo> serverSupplier;
    private final LiveProfileManager liveProfileManager;


    @Override
    public void run() {
        proxyServer.getAllPlayers().forEach(player -> {
            String intent = liveProfileManager.getProfile(player.getUniqueId()).map(LiveProfile::getJoinIntent).orElse("");

            liveProfileManager.save(new LiveProfile(
                    player.getUniqueId(),
                    player.getUsername(),
                    player.getCurrentServer().map(serverConnection -> serverConnection.getServer().getServerInfo().getName()).orElse("Unknown"),
                    Optional.ofNullable(serverSupplier.get()).map(ProxyInfo::getName).orElse(""),
                    true,
                    System.currentTimeMillis(),
                    intent
            ));
        });
    }
}
