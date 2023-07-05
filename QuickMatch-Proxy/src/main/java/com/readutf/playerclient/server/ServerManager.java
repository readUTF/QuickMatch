package com.readutf.playerclient.server;

import com.google.common.cache.Cache;
import com.readutf.quickmatch.shared.Server;
import retrofit2.Retrofit;

import java.util.List;

public class ServerManager {

    private final ServerService serverService;

    public ServerManager(Retrofit retrofit) {
        this.serverService = retrofit.create(ServerService.class);
    }

    public List<Server> getServers(boolean ignoreCache) {
        return null;
    }

}
