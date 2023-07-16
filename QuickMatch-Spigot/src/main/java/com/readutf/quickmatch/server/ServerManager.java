package com.readutf.quickmatch.server;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import retrofit2.Retrofit;

import java.util.function.Supplier;

public class ServerManager {

    private final Hermes hermes;
    private final ServerService serverService;
    private final KeepAliveTask keepAliveTask;
    private final Supplier<Server> serverSupplier;

    public ServerManager(Hermes hermes, Retrofit retrofit, Supplier<Server> serverSupplier) {
        this.hermes = hermes;
        this.serverService = retrofit.create(ServerService.class);
        this.keepAliveTask = new KeepAliveTask(hermes, serverSupplier);
        this.serverSupplier = serverSupplier;
    }

    public Server registerServer(String address, String serverType, int port) throws Exception {
        ResponseData<Server> response = RequestHelper.getResponse(serverService.registerServer(address, serverType, port));
        if (response.isError()) throw new Exception(response.getErrorReason());
        return response.getData();
    }

    public void unregisterServer(Server server) {
        RequestHelper.getResponse(serverService.unregisterServer(server.getServerId().toString()));
    }

    public void restart() {
        RequestHelper.get(serverService.registerServerFull(JsonWrapper.of(serverSupplier.get())));
    }
}
