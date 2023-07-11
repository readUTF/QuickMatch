package com.readutf.proxy;

import co.aikar.commands.VelocityCommandManager;
import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.google.inject.Inject;
import com.readutf.proxy.activity.ActivityTask;
import com.readutf.proxy.balancing.PlayerJoinBalancer;
import com.readutf.proxy.commands.ServersCommand;
import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.subscriber.Subscriber;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.quickmatch.shared.utils.RetrofitHelper;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPool;
import retrofit2.Retrofit;

import java.util.Timer;

@Plugin(id = "quickmatch", name = "QuickMatch-Proxy", version = "0.1.0-SNAPSHOT", authors = {"utf_"})
@Getter
public class QuickMatchProxy {

    private @Getter static QuickMatchProxy instance;

    private final ProxyServer proxyServer;
    private final Retrofit retrofit;
    private final Logger logger;
    private final ServerManager serverManager;
    private final JedisPool jedisPool;
    private final Hermes hermes;
    private final LiveProfileManager liveProfileManager;

    private  VelocityCommandManager commandManager;

    @Inject
    public QuickMatchProxy(ProxyServer proxyServer, Logger logger) {
        instance = this;
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
        this.serverManager = new ServerManager(proxyServer, retrofit);
        this.jedisPool = new JedisPool();
        this.hermes = Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .addListener(new Subscriber(this))
                .build();
        this.liveProfileManager = new LiveProfileManager(jedisPool);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ActivityTask(proxyServer, liveProfileManager), 0, 4000);
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent e) {
        this.commandManager = new VelocityCommandManager(proxyServer, this);
        proxyServer.getEventManager().register(this, new PlayerJoinBalancer(proxyServer, serverManager));

        commandManager.registerCommand(new ServersCommand(serverManager));
    }


}
