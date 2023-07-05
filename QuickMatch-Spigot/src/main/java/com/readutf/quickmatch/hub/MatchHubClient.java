package com.readutf.quickmatch.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.MatchClient;
import com.readutf.quickmatch.server.ServerManager;
import com.readutf.quickmatch.shared.RetrofitHelper;
import com.readutf.quickmatch.shared.Server;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.bukkit.plugin.java.JavaPlugin;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Getter
public class MatchHubClient implements MatchClient {

    private final Retrofit retrofit;
    private final ServerManager serverManager;
    private final Server server;

    public MatchHubClient(JavaPlugin javaPlugin, String serverType) throws Exception {

        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
        this.serverManager = new ServerManager(retrofit);
        this.server = serverManager.registerServer(javaPlugin.getServer().getIp(), "HUB_" + serverType.toUpperCase(), javaPlugin.getServer().getPort());

    }

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {
    }

}
