package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProxyInfo {

    private Integer id;
    private String ip;
    private int port, online;
    private long lastUpdated, registered;

    @JsonIgnore
    public String getName() {
        return String.valueOf(id);
    }

    @JsonIgnore
    public long getUptime() {
        return System.currentTimeMillis() - registered;
    }

}
