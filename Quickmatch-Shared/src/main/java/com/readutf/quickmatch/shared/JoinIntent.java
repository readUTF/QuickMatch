package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.readutf.quickmatch.shared.queue.QueueType;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class JoinIntent {

    private final String gameId;
    private final QueueType queueType;
    private final List<UUID> allPlayers;

    @JsonCreator
    public JoinIntent(@JsonProperty("gameId") String gameId, @JsonProperty("queueType") QueueType queueType, @JsonProperty("allPlayers") List<UUID> allPlayers) {
        this.gameId = gameId;
        this.queueType = queueType;
        this.allPlayers = allPlayers;
    }
}
