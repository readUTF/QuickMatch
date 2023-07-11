package com.readutf.quickmatch.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor @Getter
public class ServerPing {

    private final UUID serverId;
    private final int playerCount;

    @Override
    public String toString() {
        return "ServerPing{" +
                "serverId=" + serverId +
                ", playerCount=" + playerCount +
                '}';
    }
}
