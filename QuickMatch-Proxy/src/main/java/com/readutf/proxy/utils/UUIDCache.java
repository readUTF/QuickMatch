package com.readutf.proxy.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class UUIDCache {

    private final Map<UUID, String> idToName;
    private final Map<String, UUID> nameToId;

    public UUIDCache(JedisPool jedisPool) {
        idToName = new HashMap<>();
        nameToId = new HashMap<>();

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

}
