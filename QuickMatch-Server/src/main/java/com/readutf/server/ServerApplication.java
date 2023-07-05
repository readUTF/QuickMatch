package com.readutf.server;

import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.readutf.server.game.GameFinder;
import com.readutf.server.game.impl.AvailableGameFinder;
import com.readutf.server.publisher.Publishers;
import com.readutf.server.queue.QueueManager;
import com.readutf.server.servers.ServerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;

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

    @Bean
    public ServerManager serverManager() {
        return new ServerManager();
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
    public QueueManager queueManager(GameFinder gameFinder, Publishers publishers) {
        return new QueueManager(gameFinder, publishers);
    }

}
