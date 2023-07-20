package com.readutf.quickmatch.minigame.games;

import com.readutf.quickmatch.shared.queue.QueueType;

import java.util.List;

public interface MatchSupplier {

    List<String> getAvailableGames(QueueType queueType) throws Exception;

    String createGame(QueueType queueType) throws Exception;

}
