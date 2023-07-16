package com.readutf.quickmatch.minigame.listeners;

import com.readutf.quickmatch.minigame.api.QuickMatchGameAPI;
import com.readutf.quickmatch.shared.JoinIntent;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinIntentListenerDemo implements Listener {

    private final JavaPlugin javaPlugin;

    public JoinIntentListenerDemo(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @SneakyThrows
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {

        Player player = e.getPlayer();
        JoinIntent joinIntent = QuickMatchGameAPI.getInstance().getJoinIntent(player.getUniqueId());
        if(joinIntent == null) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Invalid join attempt");
        } else {
            e.allow();
        }

    }

}
