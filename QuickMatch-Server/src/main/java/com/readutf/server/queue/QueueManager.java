package com.readutf.server.queue;

import com.readutf.quickmatch.shared.QueueType;
import com.readutf.server.game.GameFinder;
import com.readutf.server.publisher.Publishers;

import javax.annotation.Nullable;
import java.util.*;

public class QueueManager {

    private final Timer timer;
    private final Publishers publishers;
    private final GameFinder gameFinder;
    private final Map<UUID, Queue> playerToQueue;
    private final Map<String, Queue> queueTasks;

    public QueueManager(GameFinder gameFinder, Publishers publishers) {
        this.timer = new Timer();
        this.gameFinder = gameFinder;
        this.publishers = publishers;
        this.queueTasks = new HashMap<>();
        this.playerToQueue = new HashMap<>();
    }

    /**
     * Add a player to a queue
     *
     * @param players   The players to add
     * @param queueType The queue type
     * @throws IllegalArgumentException If a player is already in the queue or if the team size is invalid
     */
    public int addToQueue(Collection<UUID> players, QueueType queueType) throws IllegalArgumentException {
        if (players.size() > queueType.getMaxTeamSize() || players.size() < queueType.getMinTeamSize())
            throw new IllegalArgumentException("Invalid team size");
        if (players.stream().anyMatch(playerToQueue::containsKey))
            throw new IllegalArgumentException("Player is already in a queue");

        Queue queue = queueTasks.computeIfAbsent(queueType.getId(), s -> new Queue(gameFinder, publishers, queueType));
        if (!queue.isRunning()) {
            timer.scheduleAtFixedRate(queue, 0, 50);
            queue.setRunning(true);
        }

        players.forEach(uuid -> playerToQueue.put(uuid, queue));
        return queue.addPlayer(players);
    }

    public @Nullable Queue getQueue(QueueType queueType) {
        return queueTasks.get(queueType);
    }

    /**
     * Get the queue task a player is in
     *
     * @param uuid The player
     * @return The {@link Queue} the player is in
     */
    public @Nullable Queue getQueue(UUID uuid) {
        return playerToQueue.get(uuid);
    }

}
