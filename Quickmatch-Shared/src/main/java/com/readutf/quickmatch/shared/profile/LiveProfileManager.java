package com.readutf.quickmatch.shared.profile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LiveProfileManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final JedisPool jedisPool;

    public LiveProfileManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Optional<LiveProfile> getProfile(UUID playerId) {
        Jedis resource = jedisPool.getResource();
        Map<String, String> data = resource.hgetAll("liveprofiles:" + playerId);
        resource.close();
        if(data.isEmpty()) return Optional.empty();
        return Optional.ofNullable(objectMapper.convertValue(data, LiveProfile.class));
    }

    public void setJoinIntent(UUID playerId, @Nonnull String joinIntent) {
        Jedis resource = jedisPool.getResource();
        resource.hset("liveprofiles:" + playerId.toString(), "joinIntent", joinIntent);
        resource.close();
    }

    public LiveProfile save(LiveProfile profile) {
        Jedis resource = jedisPool.getResource();
        resource.hmset("liveprofiles:" + profile.getPlayerId().toString(), objectMapper.convertValue(profile, new TypeReference<>() {}));
        resource.close();
        return profile;
    }

    public boolean isOnline(UUID player) {
        Boolean result = getProfile(player).map(liveProfile1 -> liveProfile1.isOnline() && System.currentTimeMillis() - liveProfile1.getLastActive() < 5000).orElse(false);
        System.out.println("isOnline(" + player + ") = " + result);
        return result;
    }
}
