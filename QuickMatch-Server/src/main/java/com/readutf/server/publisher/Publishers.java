package com.readutf.server.publisher;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.Server;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Publishers {

    private final Hermes hermes;

    public Publishers(Hermes hermes) {
        this.hermes = hermes;
    }

    public void sendServerSwitch(Collection<UUID> players, GameData gameData) {
        hermes.sendParcel("SERVER_SWITCH", Map.of("players", players, "server", gameData));
    }

}
