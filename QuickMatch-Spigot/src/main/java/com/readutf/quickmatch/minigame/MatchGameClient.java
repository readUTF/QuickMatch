package com.readutf.quickmatch.minigame;

import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.readutf.quickmatch.MatchClient;
import com.readutf.quickmatch.minigame.api.QuickMatchGameAPI;
import com.readutf.quickmatch.minigame.games.MatchRequestListener;
import com.readutf.quickmatch.minigame.games.MatchSupplier;
import com.readutf.quickmatch.minigame.listeners.JoinIntentListenerDemo;
import com.readutf.quickmatch.server.ServerManager;
import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.intent.IntentService;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.quickmatch.shared.utils.RetrofitHelper;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.subsriber.SharedSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import retrofit2.Retrofit;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class MatchGameClient implements MatchClient {

    private final JavaPlugin javaPlugin;
    private final Retrofit retrofit;
    private final ServerManager serverManager;
    private final JedisPool jedisPool;
    private final Hermes hermes;
    private final LiveProfileManager liveProfileManager;
    private final IntentService intentService;

    private @Setter MatchSupplier matchSupplier;

    public MatchGameClient(JavaPlugin javaPlugin, String serverType) throws Exception {
        this.javaPlugin = javaPlugin;
        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
        this.jedisPool = new JedisPool();
        this.hermes = Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .build();
        this.serverManager = new ServerManager(hermes, retrofit);
        this.serverManager.registerServer(javaPlugin.getServer().getIp(), "GAME_" + serverType, javaPlugin.getServer().getPort());
        hermes.addParcelListener(new MatchRequestListener(this, serverManager::getServer));
        hermes.addParcelListener(new SharedSubscriber(serverManager));
        matchSupplier = new MatchSupplier() {
            @Override
            public List<String> getAvailableGames(QueueType queueType) {
                return Collections.emptyList();
            }

            @Override
            public String createGame(QueueType queueType) {
                return null;
            }
        };
        this.liveProfileManager = new LiveProfileManager(jedisPool);
        this.intentService = retrofit.create(IntentService.class);

        new QuickMatchGameAPI(this, liveProfileManager, intentService);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JoinIntentListenerDemo(javaPlugin), javaPlugin);
    }

    @Override
    public void onDisable() {
        serverManager.unregisterServer();
    }

}
