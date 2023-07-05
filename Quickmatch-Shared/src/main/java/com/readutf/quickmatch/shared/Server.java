package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @NoArgsConstructor @Setter
public class Server {

    private UUID serverId;
    private String address;
    private String serverType;
    private int port;

    public Server(UUID serverId, String address, String serverType, int port) {
        this.serverId = serverId;
        this.address = address;
        this.serverType = serverType;
        this.port = port;
    }

    @JsonIgnore
    public String getCombinedAddress() {
        return address + ":" + port;
    }

}
