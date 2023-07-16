package com.readutf.server.queue;

import com.readutf.quickmatch.shared.*;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.server.game.GameFinder;
import com.readutf.server.joinintent.IntentManager;
import com.readutf.server.publisher.Publishers;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Queue extends TimerTask {

    private final IntentManager intentManager;
    private final LiveProfileManager liveProfileManager;
    private final QueueManager queueManager;
    private final GameFinder gameFinder;
    private final Publishers publishers;
    private final QueueType queueType;
    private final LinkedList<QueueEntry> players;

    private boolean running = false;


    public Queue(IntentManager intentManager, LiveProfileManager liveProfileManager, QueueManager queueManager, GameFinder gameFinder, Publishers publishers, QueueType queueType) {
        this.intentManager = intentManager;
        this.liveProfileManager = liveProfileManager;
        this.queueManager = queueManager;
        this.gameFinder = gameFinder;
        this.publishers = publishers;
        this.queueType = queueType;
        this.players = new LinkedList<>();
    }

    @Override
    public void run() {
        List<QueueEntry> sizeSortedList = new ArrayList<>(players.stream().sorted(Comparator.comparingInt(QueueEntry::size).thenComparing(QueueEntry::getAge)).toList());

//        System.out.println(sizeSortedList);

        for (QueueEntry queueEntry : sizeSortedList) {
            if (!isOnline(queueEntry)) {
                synchronized (players) {
                    UUID first = queueEntry.getPlayers().iterator().next();
                    removePlayer(first);
                    queueManager.removeFromQueue(first);
                }
                System.out.println("removed from queue: " + queueEntry.getPlayers());

                publishers.sendPlayerMessage(
                        PlayerMessage.builder()
                                .setPlayers(queueEntry.getPlayers())
                                .setMessages("&cYou have been removed from the queue as a member of your party is offline.")
                                .build()
                );
            }
        }

        //collect teams
        int size = 0;
        List<List<QueueEntry>> teams = new ArrayList<>();

        for (int teamSize = queueType.getMaxTeamSize(); teamSize >= queueType.getMinTeamSize(); teamSize--) {
            for (int i = 0; i < queueType.getAmountOfTeams(); i++) {
                List<QueueEntry> team = buildTeam(sizeSortedList, teamSize);
                if (team == null) break;
                sizeSortedList.removeAll(team);
                teams.add(team);
                size += team.size();
            }
        }

        if (size != queueType.getAmountOfTeams()) return; //not enough players

        GameData game;

        try {
            game = gameFinder.findGame(queueType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        if (game == null) {
            System.out.println("No game found");
            return;
        } //no game found

        //remove players from queue
        for (List<QueueEntry> team : teams) {
            for (QueueEntry queueEntry : team) {
                synchronized (players) {
                    players.remove(queueEntry);
                }
            }
        }

        List<UUID> players = teams.stream().flatMap(queueEntries -> queueEntries.stream().flatMap(queueEntry -> queueEntry.getPlayers().stream())).toList();
        intentManager.saveIntent(new JoinIntent(game.getGameId(), queueType, players));
        publishers.sendServerSwitch(players, game);
        for (UUID player : players) {
            queueManager.removeFromQueue(player);
            liveProfileManager.setJoinIntent(player, game.getGameId());
        }
    }

    public List<QueueEntry> buildTeam(List<QueueEntry> sortedEntries, int targetSize) {

        List<QueueEntry> team = new ArrayList<>();
        for (QueueEntry sortedEntry : sortedEntries) {
            if (sortedEntry.size() <= targetSize - team.size()) {
                team.add(sortedEntry);
            }
        }
        return team.size() == targetSize ? team : null;
    }


    public int addPlayer(Collection<UUID> joining) {
        synchronized (players) {
            players.add(new QueueEntry(joining));
            return players.size();
        }
    }

    public QueueEntry removePlayer(UUID playerId) {
        QueueEntry queueEntry = players.stream().filter(queueEntry1 -> queueEntry1.getPlayers().contains(playerId)).findFirst().orElse(null);
        if (queueEntry == null) return null;
        synchronized (players) {
            players.remove(queueEntry);
        }
        return queueEntry;
    }

    public boolean isOnline(QueueEntry queueEntry) {
        return queueEntry.getPlayers().stream().allMatch(liveProfileManager::isOnline);
    }

}
