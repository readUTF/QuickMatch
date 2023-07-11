package com.readutf.quickmatch.subsriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.readutf.hermes.pipline.listeners.ParcelListener;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.quickmatch.server.ServerManager;
import com.readutf.quickmatch.shared.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SharedSubscriber {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ServerManager serverManager;

    public SharedSubscriber(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @ParcelListener("API_RESTART")
    public void apiRestart() {
        serverManager.restart();
    }

    @ParcelListener("PLAYER_MESSAGE")
    public void onPlayerMessage(ParcelWrapper parcelWrapper) {
        PlayerMessage playerMessage = parcelWrapper.get(new TypeReference<>() {});

        for (UUID player : playerMessage.getPlayerIds()) {
            Optional.ofNullable(Bukkit.getPlayer(player)).ifPresent(player1 -> {
                for (String message : playerMessage.getMessages()) {
                    player1.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            });
        }

    }

}
