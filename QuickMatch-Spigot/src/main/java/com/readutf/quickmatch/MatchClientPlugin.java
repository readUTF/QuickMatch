package com.readutf.quickmatch;

import com.readutf.quickmatch.hub.MatchHubClient;
import com.readutf.quickmatch.minigame.MatchGameClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MatchClientPlugin extends JavaPlugin {

    private com.readutf.quickmatch.MatchClient matchClient;
    private boolean failedToStart;

    @Override
    public void onLoad() {
        String mode = System.getProperty("type", "hub");
        String serverType = System.getProperty("serverType", "hub");

        try {
            if (mode.equalsIgnoreCase("hub")) {
                matchClient = new MatchHubClient(this, serverType);
            } else {
                matchClient = new MatchGameClient(this, serverType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
            Bukkit.shutdown();
        }
    }

    @Override
    public void onEnable() {
        matchClient.onEnable();
    }

    @Override
    public void onDisable() {
        if (failedToStart) return;
        matchClient.onDisable();
    }

}
