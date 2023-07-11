package com.readutf.quickmatch.shared;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor @Getter
public class PlayerMessage {

    private Collection<UUID> playerIds;
    private Collection<String> messages;

    public PlayerMessage(Collection<UUID> playerIds, Collection<String> messages) {
        this.playerIds = playerIds;
        this.messages = messages;
    }

    public PlayerMessage(UUID playerId, Collection<String> messages) {
        this.playerIds = List.of(playerId);
        this.messages = messages;
    }

    public PlayerMessage(UUID playerId, String... messages) {
        this.playerIds = List.of(playerId);
        this.messages = Arrays.asList(messages);
    }

    public PlayerMessage(Collection<UUID> playerIds, String... messages) {
        this.playerIds = playerIds;
        this.messages = Arrays.asList(messages);
    }
}
