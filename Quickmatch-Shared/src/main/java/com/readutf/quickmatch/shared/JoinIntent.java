package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinIntent {

    private final String gameId;
    private final QueueType queueType;

    @JsonCreator
    public JoinIntent(@JsonProperty("gameId") String gameId, @JsonProperty("queueType") QueueType queueType) {
        this.gameId = gameId;
        this.queueType = queueType;
    }
}
