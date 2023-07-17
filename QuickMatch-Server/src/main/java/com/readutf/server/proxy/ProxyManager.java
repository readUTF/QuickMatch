package com.readutf.server.proxy;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.PlayerMessage;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.ProxyPing;
import lombok.Getter;

import java.util.*;

public class ProxyManager {

    private final Hermes hermes;
    private @Getter final Map<Integer, ProxyInfo> activeProxies;

    public ProxyManager(Hermes hermes, Timer timer) {
        this.hermes = hermes;
        this.activeProxies = new HashMap<>();
        timer.scheduleAtFixedRate(new ProxyTask(this), 0, 1000);
    }

    public ProxyInfo registerProxy(Integer proxyId, String address, int port) {
        ProxyInfo proxyInfo = new ProxyInfo(proxyId, address, port, 0, System.currentTimeMillis(), System.currentTimeMillis());
        activeProxies.put(proxyId, proxyInfo);

        hermes.sendParcel("PLAYER_MESSAGE", new PlayerMessage.Builder()
                .setMessages("&9&lGM &8» &7A new proxy has registered with id &b%s".formatted(proxyId.toString()))
                .setPermission("quickmatch.server.notify").build());

        return proxyInfo;
    }

    public void unregisterProxy(Integer proxyId) {
        activeProxies.remove(proxyId);

        hermes.sendParcel("PLAYER_MESSAGE", new PlayerMessage.Builder()
                .setMessages("&9&lGM &8» &7Proxy &b%s &7has unregistered".formatted(proxyId.toString()))
                .setPermission("quickmatch.server.notify")
                .build());

    }

    public List<ProxyInfo> getProxies() {
        return new ArrayList<>(activeProxies.values());
    }

    public void handlePing(ProxyPing proxyPing) {
        Optional.ofNullable(activeProxies.get(proxyPing.getProxyId())).ifPresent(proxyInfo -> {
            proxyInfo.setOnline(proxyPing.getOnline());
            proxyInfo.setLastUpdated(System.currentTimeMillis());
        });
    }
}
