package com.readutf.quickmatch.minigame.games;

import com.github.readutf.hermes.pipline.listeners.ParcelListener;
import com.github.readutf.hermes.wrapper.ParcelWrapper;
import com.readutf.quickmatch.minigame.MatchGameClient;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MatchRequestListener {

    private final MatchGameClient matchGameClient;
    private final Supplier<Server> serverSupplier;

    public MatchRequestListener(MatchGameClient matchGameClient, Supplier<Server> serverSupplier) {
        this.matchGameClient = matchGameClient;
        this.serverSupplier = serverSupplier;
    }

    @ParcelListener("FIND_GAME")
    public List<GameData> onFindGame(ParcelWrapper parcelWrapper) {
        QueueType queueType;
        try {
             queueType = parcelWrapper.get(QueueType.class);
        } catch (Exception e) {
            System.out.println("Failed to read QueueType.class from FIND_GAME parcel");
            return new ArrayList<>();
        }
        List<String> availableGames;
        try {
            availableGames = matchGameClient.getMatchSupplier().getAvailableGames(queueType);
        } catch (Exception e) {
            System.out.println("Failed to get available games");
            return new ArrayList<>();
        }

        return availableGames.stream().map(s -> new GameData(serverSupplier.get(), s, System.currentTimeMillis())).collect(Collectors.toList());
    }

}
