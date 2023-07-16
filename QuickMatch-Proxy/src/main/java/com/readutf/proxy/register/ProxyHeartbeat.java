package com.readutf.proxy.register;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.ProxyPing;
import com.readutf.quickmatch.shared.serializers.ProxyPingSerializer;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AllArgsConstructor;

import java.util.TimerTask;

@AllArgsConstructor
public class ProxyHeartbeat extends TimerTask {

    private final ProxyManager proxyManager;
    private final ProxyServer proxyServer;
    private final Hermes hermes;

    @Override
    public void run() {
        ProxyInfo proxyInfo = proxyManager.getProxyInfo();
        ProxyPing proxyPing = new ProxyPing(proxyInfo.getId(), proxyServer.getPlayerCount());
        hermes.sendParcel("PROXY_PING", proxyPing, new ProxyPingSerializer());
    }
}
