package com.readutf.server.subscriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.readutf.hermes.pipline.listeners.ParcelListener;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.quickmatch.shared.ServerPing;
import com.readutf.quickmatch.shared.serializers.ServerPingSerializer;
import com.readutf.server.servers.ServerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Subscriber {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ServerManager serverManager;

    public Subscriber(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @ParcelListener("SERVER_PING")
    public void onServerPing(ParcelWrapper parcelWrapper) {
        ServerPing serverPing;
        try {
            serverPing = parcelWrapper.get(new ServerPingSerializer());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        serverManager.handlePing(serverPing);
    }

}
