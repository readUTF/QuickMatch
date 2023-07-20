package com.readutf.server.game.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.server.game.GameFinder;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class AvailableGameFinder implements GameFinder {

    private final Hermes hermes;

    @Override
    public List<GameData> findGames(QueueType queueType) throws Exception {

        List<GameData> gameDataList = new ArrayList<>();
        AtomicInteger serverResponses = new AtomicInteger();
        hermes.sendParcel("FIND_GAME", queueType, parcelWrapper -> {
            serverResponses.incrementAndGet();
            gameDataList.addAll(parcelWrapper.get(new TypeReference<>() {}));
        });

        Thread.sleep(250);

        if (serverResponses.get() == 0) {
            Thread.sleep(750);
            if (serverResponses.get() == 0) throw new Exception("No servers responded");
        }

        return gameDataList;
    }

    @Nullable
    @Override
    public GameData findGame(QueueType queueType) throws Exception {
        return findGames(queueType).stream().findFirst().orElse(null);
    }
}
