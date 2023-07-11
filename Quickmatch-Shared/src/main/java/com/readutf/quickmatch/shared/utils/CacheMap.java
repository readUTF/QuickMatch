package com.readutf.quickmatch.shared.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CacheMap<A, B> {

    private final HashMap<A, CachedValue<B>> cacheMap;
    private final Provider<A, B> provider;
    private final long cacheDuration;

    @Setter private static boolean debug;

    public CacheMap(long cacheDuration, Provider<A, B> provider) {
        this.provider = provider;
        this.cacheDuration = cacheDuration;
        cacheMap = new HashMap<>();
    }

    public B get(A key, boolean refresh) {
        if (cacheMap.containsKey(key) && !cacheMap.get(key).hasExpired() && !refresh) return cacheMap.get(key).getCachedData();
        if(debug) Logger.getLogger(getClass().getSimpleName()).warning("Got from provider");
        B value = provider.getValue(key);
        if(value == null) return null;

        cacheMap.put(key, new CachedValue<>(value, cacheDuration));
        return value;
    }

    public Pair<B, Long> getWithCachedTime(A key, boolean refresh) {
        if (cacheMap.containsKey(key) && !cacheMap.get(key).hasExpired() && !refresh) return Pair.of(cacheMap.get(key).getCachedData(), cacheMap.get(key).getCachedAt());
        if(debug) Logger.getLogger(getClass().getSimpleName()).warning("Got from provider");
        B value = provider.getValue(key);
        if(value == null) return null;

        cacheMap.put(key, new CachedValue<>(value, cacheDuration));
        return Pair.of(value, Instant.now().toEpochMilli());
    }

    public void refresh(A key) {
        cacheMap.put(key, new CachedValue<>(provider.getValue(key), cacheDuration));
    }

    public Set<A> keySet() {
        return cacheMap.keySet();
    }

    public Set<B> values() {
        return cacheMap.values().stream().map(CachedValue::getCachedData).collect(Collectors.toSet());
    }

    public void cache(A key, B data) {
        cacheMap.put(key, new CachedValue<>(data, cacheDuration));
    }

    public boolean isCached(A key) {
        return cacheMap.containsKey(key);
    }

    public interface Provider<A, B> {

        B getValue(A key);
    }

    public static class CachedValue<B> {

        long cacheDuration;

        public CachedValue(B cachedValue, long cacheDuration) {
            this.cachedData = cachedValue;
            this.cachedAt = System.currentTimeMillis();
            this.cacheDuration = cacheDuration;
        }

        @Getter
        B cachedData;

        @Getter
        long cachedAt;

        boolean hasExpired() {
            if (cacheDuration == -1) return false;
            return System.currentTimeMillis() - cachedAt > cacheDuration;
        }

    }

}
