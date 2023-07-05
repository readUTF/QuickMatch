package com.readutf.quickmatch.server;

import com.readutf.quickmatch.utils.RequestHelper;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import retrofit2.Retrofit;

public class ServerManager {

    private final ServerService serverService;

    public ServerManager(Retrofit retrofit) {
        this.serverService = retrofit.create(ServerService.class);
    }

    public Server registerServer(String address, String serverType, int port) throws Exception {
        ResponseData<Server> response = RequestHelper.getResponse(serverService.registerServer(address, serverType, port));
        if (response.isError()) throw new Exception(response.getErrorReason());
        return response.getData();
    }

    public void unregisterServer(Server server) {
        RequestHelper.getResponse(serverService.unregisterServer(server.getServerId().toString()));
    }
}
