package com.readutf.server;

import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.server.analytics.AnalyticsManager;
import com.readutf.server.commands.CommandManager;
import com.readutf.server.commands.impl.ServersCommand;
import com.readutf.server.config.SettingsConfig;
import com.readutf.server.game.GameFinder;
import com.readutf.server.game.impl.AvailableGameFinder;
import com.readutf.server.game.impl.CreatorGameFinder;
import com.readutf.server.joinintent.IntentManager;
import com.readutf.server.proxy.ProxyManager;
import com.readutf.server.publisher.Publishers;
import com.readutf.server.queue.QueueManager;
import com.readutf.server.servers.ServerManager;
import com.readutf.server.subscriber.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Timer;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public SettingsConfig settingsConfig() {
        return new SettingsConfig();
    }

    @Bean
    public Hermes hermes(SettingsConfig settingsConfig) {
        JedisPool jedisPool = settingsConfig.getJedisPool();
        return Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .build();
    }

    @Bean
    public IntentManager intentManager() {
        return new IntentManager();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onReady(ContextRefreshedEvent event) {
        Hermes hermes = event.getApplicationContext().getBean(Hermes.class);
        hermes.sendParcel("API_RESTART", new HashMap<>());
    }

    @Bean
    public LiveProfileManager liveProfileManager(SettingsConfig settingsConfig) {
        return new LiveProfileManager(settingsConfig.getJedisPool());
    }

    @Bean
    public CommandManager commandManager(ServerManager serverManager) {
        return new CommandManager(new ServersCommand(serverManager));
    }

    @Bean
    public Timer timer() {
        return new Timer();
    }

    @Bean
    public ProxyManager proxyManager(Hermes hermes, Timer timer) {
        return new ProxyManager(hermes, timer);
    }

    @Bean
    public ServerManager serverManager(Hermes hermes, Timer timer, ProxyManager proxyManager) {
        ServerManager serverManager = new ServerManager(hermes, timer);
        hermes.addParcelListener(new Subscriber(serverManager, proxyManager));
        return serverManager;
    }

    @Bean
    public GameFinder gameFinder(ServerManager serverManager, Hermes hermes) {
        return new CreatorGameFinder(serverManager, hermes);
    }

    @Bean
    public Publishers publishers(Hermes hermes) {
        return new Publishers(hermes);
    }

    @Bean
    public AnalyticsManager analyticsManager(MongoClient mongoClient) {
        return new AnalyticsManager(mongoClient.getDatabase("QuickMatch"));
    }

    @Bean
    public QueueManager queueManager(Timer timer,
                                     Publishers publishers,
                                     LiveProfileManager liveProfileManager,
                                     AnalyticsManager analyticsManager,
                                     GameFinder gameFinder,
                                     IntentManager intentManager) {
        return new QueueManager(timer, publishers, liveProfileManager, analyticsManager, gameFinder, intentManager);
    }

}
