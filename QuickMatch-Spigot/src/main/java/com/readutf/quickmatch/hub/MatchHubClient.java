package com.readutf.quickmatch.hub;

import co.aikar.commands.PaperCommandManager;
import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.senders.impl.JedisParcelSender;
import com.github.readutf.hermes.subscribers.impl.JedisParcelSubscriber;
import com.readutf.quickmatch.MatchClient;
import com.readutf.quickmatch.hub.commands.QueueCommand;
import com.readutf.quickmatch.queue.QueueManager;
import com.readutf.quickmatch.server.ServerManager;
import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.utils.RetrofitHelper;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.subsriber.SharedSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MatchHubClient implements MatchClient {

    private final JavaPlugin javaPlugin;
    private final Retrofit retrofit;
    private final ServerManager serverManager;
    private final Hermes hermes;
    private final JedisPool jedisPool;
    private PaperCommandManager commandManager;
    private final QueueManager queueManager;

    public MatchHubClient(JavaPlugin javaPlugin, String serverType) throws Exception {
        this.javaPlugin = javaPlugin;
        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
        this.jedisPool = new JedisPool();
        this.hermes = Hermes.builder()
                .prefix("quickmatch")
                .parcelSender(new JedisParcelSender(jedisPool))
                .parcelSubscriber(new JedisParcelSubscriber(jedisPool))
                .build();
        this.queueManager = new QueueManager(retrofit);
        this.serverManager = new ServerManager(hermes, retrofit);
        serverManager.registerServer(javaPlugin.getServer().getIp(), "HUB_" + serverType.toUpperCase(), javaPlugin.getServer().getPort());
        this.hermes.addParcelListener(new SharedSubscriber(serverManager));
    }

    @Override
    public void onEnable() {
        this.commandManager = new PaperCommandManager(javaPlugin);
        commandManager.getCommandCompletions().registerAsyncCompletion("queues", c -> {
            List<String> list = new ArrayList<>();
            for (QueueType queueType : queueManager.getQueues(false)) {
                String id = queueType.getId();
                list.add(id);
            }
            return list;
        });


        commandManager.registerCommand(new QueueCommand(queueManager));
    }

    @Override
    public void onDisable() {
    }

}
