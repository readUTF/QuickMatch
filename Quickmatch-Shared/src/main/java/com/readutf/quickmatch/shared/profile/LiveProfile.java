package com.readutf.quickmatch.shared.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Getter
@ToString
public class LiveProfile {

    private final UUID playerId;
    private final boolean online;
    private final String username, lastServer, lastProxy, joinIntent;
    private final Long lastActive;

    @JsonCreator
    public LiveProfile(
            @JsonProperty("playerId") UUID playerId,
            @JsonProperty("username") @Nonnull String username,
            @JsonProperty("lastServer") @Nonnull String lastServer,
            @JsonProperty("lastProxy") @Nonnull String lastProxy,
            @JsonProperty("online") boolean online,
            @JsonProperty("lastActive") @Nullable Long lastActive,
            @JsonProperty("joinIntent") @Nullable String joinIntent
    ) {
        this.playerId = playerId;
        this.username = username;
        this.lastServer = lastServer;
        this.lastProxy = lastProxy;
        this.online = online;
        this.lastActive = lastActive;
        this.joinIntent = joinIntent;
    }
}
