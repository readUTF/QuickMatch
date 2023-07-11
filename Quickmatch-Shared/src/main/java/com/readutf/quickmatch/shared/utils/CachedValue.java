package com.readutf.quickmatch.shared.utils;

import java.util.function.Supplier;

public class CachedValue<T> {

    private final Supplier<T> supplier;
    private final long cacheTime;

    private T lastValue;
    private long lastUpdated;

    public CachedValue(Supplier<T> supplier, long cacheTime) {
        this.supplier = supplier;
        this.cacheTime = cacheTime;
    }

    public T getValue(boolean ignoreCache) {
        if(ignoreCache || System.currentTimeMillis() - lastUpdated > cacheTime) {
            lastUpdated = System.currentTimeMillis();
            return (lastValue = supplier.get());
        }
        return lastValue;
    }

    public T getCachedValue() {
        return lastValue;
    }

    public void setCachedValue(T value) {
        lastValue = value;
    }

}
