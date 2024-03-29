package com.readutf.server.servers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/private/servers")
@AllArgsConstructor
public class ServerApi {

    private static int idTracker = 1;

    private final ObjectMapper objectMapper;
    private final ServerManager serverManager;

    /**
     * API end point for registering a server
     *
     * @param address the address of the server
     * @param port    the port of the server
     * @return the server object
     */
    @PutMapping("register")
    public ResponseData<Server> registerServer(String address, String serverType, int port) {
        Server server = new Server(idTracker++, address, serverType, port, 0, System.currentTimeMillis());

        serverManager.registerServer(server);
        return ResponseData.success(server);
    }

    @SneakyThrows
    @PutMapping("registerFull")
    public ResponseData<Server> registerServerFull(String object) {
        Server server = objectMapper.readValue(object, Server.class);

        server.setServerId(idTracker++);

        serverManager.registerServer(server);
        return ResponseData.success(server);
    }


    /**
     * API end point for getting a list of servers
     *
     * @return a list of servers
     */
    @GetMapping("list")
    public ResponseData<List<Server>> listServers() {
        return ResponseData.success(serverManager.getServers());
    }

    /**
     * API end point for unregistering a server
     *
     * @param serverId the id of the server
     * @return whether the server was unregistered
     */
    @DeleteMapping("unregister")
    public ResponseData<Boolean> unregisterServer(@RequestParam("serverId") Integer serverId) {
        return ResponseData.success(serverManager.unregisterServer(serverId));
    }

}
