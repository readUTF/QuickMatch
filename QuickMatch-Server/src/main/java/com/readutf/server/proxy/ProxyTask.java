package com.readutf.server.proxy;

import com.readutf.quickmatch.shared.ProxyInfo;
import lombok.AllArgsConstructor;

import java.util.TimerTask;

@AllArgsConstructor
public class ProxyTask extends TimerTask {

    private final ProxyManager proxyManager;

    @Override
    public void run() {
        for (ProxyInfo activeProxy : proxyManager.getActiveProxies().values()) {
            if(System.currentTimeMillis() - activeProxy.getLastUpdated() > 10_000) {
                proxyManager.unregisterProxy(activeProxy.getId());
            }
        }
    }
}
