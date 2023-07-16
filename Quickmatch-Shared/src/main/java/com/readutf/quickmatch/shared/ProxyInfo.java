package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProxyInfo {

    private UUID id;
    private String ip;
    private int port, online;
    private long lastUpdated, registered;

    @JsonIgnore
    public String getName() {
        return id.toString().substring(0, 13);
    }

    @JsonIgnore
    public long getUptime() {
        return System.currentTimeMillis() - registered;
    }

}
