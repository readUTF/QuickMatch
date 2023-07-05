package com.readutf.quickmatch.minigame;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MatchClientPlugin extends JavaPlugin {

    private MatchGameClient matchGameClient;
    private boolean failedToStart;

    @Override
    public void onLoad() {
        try {
            matchGameClient = new MatchGameClient(this);
        } catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
            Bukkit.shutdown();
        }
    }

    @Override
    public void onEnable() {
        matchGameClient.onEnable();
    }

    @Override
    public void onDisable() {
        if(failedToStart) return;
        matchGameClient.onDisable();
    }

}
