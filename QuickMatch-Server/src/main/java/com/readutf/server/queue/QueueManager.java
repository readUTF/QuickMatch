package com.readutf.server.queue;

import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
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
    private final LiveProfileManager liveProfileManager;

    public QueueManager(LiveProfileManager liveProfileManager, Timer timer, GameFinder gameFinder, Publishers publishers) {
        this.timer = timer;
        this.gameFinder = gameFinder;
        this.publishers = publishers;
        this.queueTasks = new HashMap<>();
        this.playerToQueue = new HashMap<>();
        this.liveProfileManager = liveProfileManager;
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
            throw new IllegalArgumentException("Your team size is invalid");
        if (players.stream().anyMatch(playerToQueue::containsKey))
            throw new IllegalArgumentException("You are already in a queue");

        Queue queue = queueTasks.computeIfAbsent(queueType.getId(), s -> new Queue(liveProfileManager, this, gameFinder, publishers, queueType));
        if (!queue.isRunning()) {
            timer.scheduleAtFixedRate(queue, 0, 1000);
            queue.setRunning(true);
        } else {
            queue.run();
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

    public void removeFromQueue(UUID playerId) {
        playerToQueue.remove(playerId);
    }
}
