package com.readutf.proxy.register;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.utils.CachedValue;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

@Getter
public class ProxyManager {

    private final ProxyServer proxyServer;
    private final ProxyService proxyService;

    private ProxyInfo proxyInfo;
    private CachedValue<List<ProxyInfo>> proxies;

    public ProxyManager(Hermes hermes, Timer timer, ProxyServer proxyServer, Retrofit retrofit) {
        this.proxyServer = proxyServer;
        this.proxyService = retrofit.create(ProxyService.class);
        this.proxyInfo = registerProxy();
        if (proxyInfo == null) throw new RuntimeException("Failed to register proxy!");
        proxies = new CachedValue<>(() -> RequestHelper.get(proxyService.getProxies()), TimeUnit.SECONDS.toMillis(2));
        timer.scheduleAtFixedRate(new ProxyHeartbeat(this, proxyServer, hermes), 0, TimeUnit.SECONDS.toMillis(5));
    }

    public ProxyInfo registerProxy() {
        return RequestHelper.get(proxyService.registerProxy("localhost", proxyServer.getBoundAddress().getPort()));
    }

    public List<ProxyInfo> getAllProxies() {
        return proxies.getValue(true);
    }

    public void restart() {
        this.proxyInfo = registerProxy();
    }

}
