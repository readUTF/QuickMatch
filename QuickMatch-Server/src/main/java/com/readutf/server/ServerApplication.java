package com.readutf.server;

import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.server.commands.CommandManager;
import com.readutf.server.commands.impl.ServersCommand;
import com.readutf.server.game.GameFinder;
import com.readutf.server.game.impl.AvailableGameFinder;
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
    public Hermes hermes() {
        JedisPool jedisPool = new JedisPool();
        return Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .build();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onReady(ContextRefreshedEvent event) {
        Hermes hermes = event.getApplicationContext().getBean(Hermes.class);
        hermes.sendParcel("API_RESTART", new HashMap<>());
    }

    @Bean
    public LiveProfileManager liveProfileManager() {
        JedisPool jedisPool = new JedisPool();
        return new LiveProfileManager(jedisPool);
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
    public ServerManager serverManager(Hermes hermes, Timer timer) {
        ServerManager serverManager = new ServerManager(hermes, timer);
        hermes.addParcelListener(new Subscriber(serverManager));
        return serverManager;
    }

    @Bean
    public GameFinder gameFinder(Hermes hermes) {
        return new AvailableGameFinder(hermes);
    }

    @Bean
    public Publishers publishers(Hermes hermes) {
        return new Publishers(hermes);
    }


    @Bean
    public QueueManager queueManager(Timer timer, LiveProfileManager liveProfileManager, GameFinder gameFinder, Publishers publishers) {
        return new QueueManager(liveProfileManager, timer, gameFinder, publishers);
    }

}
