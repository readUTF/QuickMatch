package com.readutf.server.game;

import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.QueueType;

import javax.annotation.Nullable;
import java.util.List;

public interface GameFinder {

    List<GameData> findGames(QueueType queueType) throws Exception;

    @Nullable GameData findGame(QueueType queueType) throws Exception;

}