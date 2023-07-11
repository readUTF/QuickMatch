package com.readutf.proxy.activity;

import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;

import java.util.TimerTask;

@AllArgsConstructor
public class ActivityTask extends TimerTask {

    private final ProxyServer proxyServer;
    private final LiveProfileManager liveProfileManager;

    @Override
    public void run() {
        proxyServer.getAllPlayers().forEach(player -> {
            liveProfileManager.save(new LiveProfile(
                    player.getUniqueId(),
                    player.getUsername(),
                    player.getCurrentServer().map(serverConnection -> serverConnection.getServer().getServerInfo().getName()).orElse("Unknown"),
                    System.currentTimeMillis(),
                    ""
            ));
        });
    }
}
