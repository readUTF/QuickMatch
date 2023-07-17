package com.readutf.proxy.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.proxy.QuickMatchProxy;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

public class UUIDCache {

    private final JedisPool jedisPool;
    private final Map<UUID, String> idToName;
    private final Map<String, UUID> nameToId;

    public UUIDCache(QuickMatchProxy proxy, JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.idToName = new HashMap<>();
        this.nameToId = new HashMap<>();

        Jedis resource = jedisPool.getResource();
        Map<String, String> uuidCache = resource.hgetAll("UUIDCache");
        resource.close();

        uuidCache.forEach((s, s2) -> {
            idToName.put(UUID.fromString(s), s2);
            nameToId.put(s2, UUID.fromString(s));
        });
    }

    public @Nullable UUID getPlayerId(String name) {
        return nameToId.get(name);
    }

    public @Nullable String getPlayerName(UUID uuid) {
        return idToName.get(uuid);
    }

    @Subscribe
    public void onJoin(ServerConnectedEvent e) {
        if(e.getPreviousServer().isPresent()) return;

        Player player = e.getPlayer();
        idToName.put(player.getUniqueId(), player.getUsername());
        nameToId.put(player.getUsername(), player.getUniqueId());
        try (ForkJoinPool pool = ForkJoinPool.commonPool()) {
            pool.submit(() -> {
                Jedis resource = jedisPool.getResource();
                resource.hset("UUIDCache", player.getUniqueId().toString(), player.getUsername());
                resource.close();
            });
        }

    }

}
