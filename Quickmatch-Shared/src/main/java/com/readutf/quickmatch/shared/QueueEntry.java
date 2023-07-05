package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collection;
import java.util.UUID;

@Getter
public class QueueEntry {

    private long joinTime;
    private final Collection<UUID> players;

    public QueueEntry(Collection<UUID> players) {
        this.joinTime = System.currentTimeMillis();
        this.players = players;
    }

    public long getAge() {
        return System.currentTimeMillis() - joinTime;
    }

    public int size() {
        return players.size();
    }

    @Override
    public String toString() {
        return "QueueEntry{" + players + '}';
    }
}
