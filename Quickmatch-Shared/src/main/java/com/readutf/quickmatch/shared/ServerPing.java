package com.readutf.quickmatch.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ServerPing {

    private final int serverId;
    private final int playerCount;
    private final double tps;

    public ServerPing(int serverId, int playerCount, double tps) {
        this.serverId = serverId;
        this.playerCount = playerCount;
        this.tps = tps;
    }

    @Override
    public String toString() {
        return "ServerPing{" +
                "serverId=" + serverId +
                ", playerCount=" + playerCount +
                '}';
    }
}
