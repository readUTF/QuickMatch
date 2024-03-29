package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
public class Server {

    private int serverId;
    private String address;
    private String serverType;
    private int port;
    private int playerCount;
    private long lastPing;
    private long startupTime;
    private double tps;

    public Server(int serverId, String address, String serverType, int port, int playerCount, long lastPing) {
        this.serverId = serverId;
        this.address = address;
        this.serverType = serverType;
        this.port = port;
        this.playerCount = playerCount;
        this.lastPing = lastPing;
        this.startupTime = System.currentTimeMillis();
        this.tps = 20;
    }

    @JsonIgnore
    public String getCombinedAddress() {
        return address + ":" + port;
    }

}
