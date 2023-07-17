package com.readutf.quickmatch.server;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import lombok.Getter;
import retrofit2.Retrofit;

import java.util.function.Supplier;

public class ServerManager {

    private final Hermes hermes;
    private final ServerService serverService;
    private final KeepAliveTask keepAliveTask;

    private @Getter Server server;

    public ServerManager(Hermes hermes, Retrofit retrofit) {
        this.hermes = hermes;
        this.serverService = retrofit.create(ServerService.class);
        this.keepAliveTask = new KeepAliveTask(hermes, this::getServer);
    }

    public void registerServer(String address, String serverType, int port) throws Exception {
        ResponseData<Server> response = RequestHelper.getResponse(serverService.registerServer(address, serverType, port));
        if (response.isError()) throw new Exception(response.getErrorReason());

        this.server = response.getData();
    }

    public void unregisterServer() {
        RequestHelper.getResponse(serverService.unregisterServer(server.getServerId()));
    }

    public void restart() {
        server = RequestHelper.get(serverService.registerServerFull(JsonWrapper.of(server)));
    }
}
