package com.readutf.server.game.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.readutf.hermes.Hermes;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.quickmatch.shared.Server;
import com.readutf.server.game.GameFinder;
import com.readutf.server.servers.ServerManager;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@AllArgsConstructor
public class CreatorGameFinder implements GameFinder {

    private final ServerManager serverManager;
    private final Hermes hermes;


    @Override
    public List<GameData> findGames(QueueType queueType) {

        List<Server> gameServers = serverManager.getServers().stream().filter(server -> server.getServerType().startsWith("GAME")).toList();
        if(gameServers.isEmpty()) return List.of();

        for (int i = 0; i < Math.min(10, gameServers.size()); i++) {
            System.out.println("attempt: " + i);
            Server server = serverManager.getServers().get(i);

            CompletableFuture<ParcelWrapper> response = hermes.sendParcelFuture("CREATE_GAME", Map.of(
                    "queueType", queueType,
                    "serverId", server.getServerId()
            ));
            ParcelWrapper parcelWrapper = getOrNull(response, 2000);
            GameData gameData = parcelWrapper.get(new TypeReference<>() {});

            if(gameData != null) return List.of(gameData);
        }

        return Collections.emptyList();
    }

    public <T> T getOrNull(CompletableFuture<T> future, int millis) {
        try {
            return future.get(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            return null;
        }
    }

}
