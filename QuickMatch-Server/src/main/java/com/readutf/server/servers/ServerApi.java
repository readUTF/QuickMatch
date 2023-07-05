package com.readutf.server.servers;

import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/private" +
        "/servers")
@AllArgsConstructor
public class ServerApi {

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
        Server server = new Server(UUID.randomUUID(), address, serverType, port);

        serverManager.registerServer(server);
        return ResponseData.success(server);
    }

    @DeleteMapping("unregister")
    public ResponseData<Boolean> unregisterServer(@RequestParam("serverId") UUID serverId) {
        return ResponseData.success(serverManager.unregisterServer(serverId));
    }

}
