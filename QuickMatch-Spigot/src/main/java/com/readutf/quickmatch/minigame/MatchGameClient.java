package com.readutf.quickmatch.minigame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.readutf.quickmatch.MatchClient;
import com.readutf.quickmatch.minigame.games.MatchRequestListener;
import com.readutf.quickmatch.minigame.games.MatchSupplier;
import com.readutf.quickmatch.minigame.server.ServerManager;
import com.readutf.quickmatch.minigame.utils.DebugInterceptor;
import com.readutf.quickmatch.shared.Server;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.UUID;

@Getter
public class MatchGameClient implements MatchClient {

    private final Retrofit retrofit;
    private final ServerManager serverManager;
    private final Server server;
    private final JedisPool jedisPool;
    private final Hermes hermes;

    private MatchSupplier matchSupplier;

    public MatchGameClient(JavaPlugin javaPlugin, String serverType) throws Exception {
        this.retrofit = setupRetrofit();
        this.serverManager = new ServerManager(retrofit);
        this.server = serverManager.registerServer(javaPlugin.getServer().getIp(), "GAME_" + serverType, javaPlugin.getServer().getPort());
        this.jedisPool = new JedisPool();

        matchSupplier = queueType -> List.of(UUID.randomUUID().toString());

        this.hermes = Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .addListener(new MatchRequestListener(this))
                .build();
    }

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {
        serverManager.unregisterServer(server);
    }

    public Retrofit setupRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new DebugInterceptor());

        return new Retrofit.Builder()
                .baseUrl("http://localhost:8080/api/private/")
                .client(builder.build())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
    }

}
