package com.readutf.server.queue;

import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.server.analytics.AnalyticsManager;
import com.readutf.server.game.GameFinder;
import com.readutf.server.joinintent.IntentManager;
import com.readutf.server.publisher.Publishers;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.*;

@AllArgsConstructor
@Getter
public class QueueManager {

    private final Timer timer;
    private final Publishers publishers;
    private final LiveProfileManager liveProfileManager;
    private final AnalyticsManager analyticsManager;
    private final GameFinder gameFinder;
    private final IntentManager intentManager;

    private final Map<UUID, Queue> playerToQueue = new HashMap<>();
    private final Map<String, Queue> queues = new HashMap<>();

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

        Queue queue = queues.computeIfAbsent(queueType.getId(), s -> new Queue(this, gameFinder, queueType));
        if (!queue.isRunning()) {
            timer.scheduleAtFixedRate(queue, 0, 1000);
            queue.setRunning(true);
        } else {
            queue.run();
        }
        analyticsManager.getQueueActivityTracker().onJoin(queue);

        players.forEach(uuid -> playerToQueue.put(uuid, queue));
        return queue.addPlayer(players);
    }

    public void removeFromQueue(UUID playerId) {
        playerToQueue.remove(playerId);
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

    public List<Queue> getQueues() {
        return new ArrayList<>(queues.values());
    }
}
