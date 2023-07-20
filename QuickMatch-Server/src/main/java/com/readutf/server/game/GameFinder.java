package com.readutf.server.game;

import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.queue.QueueType;

import javax.annotation.Nullable;
import java.util.List;

public interface GameFinder {

    List<GameData> findGames(QueueType queueType) throws Exception;

    @Nullable default GameData findGame(QueueType queueType) throws Exception {
        return findGames(queueType).stream().findFirst().orElse(null);
    }

}
