package com.readutf.proxy;

import co.aikar.commands.VelocityCommandManager;
import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.google.inject.Inject;
import com.readutf.proxy.activity.ActivityListener;
import com.readutf.proxy.activity.ActivityTask;
import com.readutf.proxy.activity.UserInfoCommand;
import com.readutf.proxy.balancing.PlayerJoinBalancer;
import com.readutf.proxy.commands.ProxyCommand;
import com.readutf.proxy.commands.ServersCommand;
import com.readutf.proxy.config.ConfigManager;
import com.readutf.proxy.register.ProxyManager;
import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.subscriber.Subscriber;
import com.readutf.proxy.utils.UUIDCache;
import com.readutf.quickmatch.shared.Server;
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

import java.io.File;
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
    private final UUIDCache uuidCache;
    private final Hermes hermes;
    private final ProxyManager proxyManager;
    private final LiveProfileManager liveProfileManager;
    private final ConfigManager configManager;

    private VelocityCommandManager commandManager;

    @Inject
    public QuickMatchProxy(ProxyServer proxyServer, Logger logger, File dataFolder) {
        instance = this;
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.jedisPool = new JedisPool();
        this.uuidCache = new UUIDCache(this, jedisPool);
        this.hermes = Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .addListener(new Subscriber(this))
                .build();
        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
        this.proxyManager = new ProxyManager(hermes, new Timer(), proxyServer, retrofit);
        this.serverManager = new ServerManager(proxyServer, retrofit);
        this.liveProfileManager = new LiveProfileManager(jedisPool);
        this.configManager = new ConfigManager(dataFolder);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ActivityTask(proxyServer, proxyManager::getProxyInfo, liveProfileManager), 0, 4000);


    }

    @Subscribe
    public void onInit(ProxyInitializeEvent e) {
        this.commandManager = new VelocityCommandManager(proxyServer, this);
        this.commandManager.registerCommand(new ServersCommand(serverManager));
        this.commandManager.registerCommand(new ProxyCommand(proxyManager));
        this.commandManager.registerCommand(new UserInfoCommand(uuidCache, liveProfileManager));
        this.commandManager.getCommandCompletions().registerAsyncCompletion("servers", c ->
                serverManager.getServers(false).stream().map(server -> String.valueOf(server.getServerId())).toList());

        proxyServer.getEventManager().register(this, new PlayerJoinBalancer(proxyServer, serverManager));
        proxyServer.getEventManager().register(this, new ActivityListener(proxyManager::getProxyInfo, liveProfileManager));
        proxyServer.getEventManager().register(this, uuidCache);

    }


}
