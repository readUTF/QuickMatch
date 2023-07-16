package com.readutf.quickmatch.minigame.api;

import com.readutf.quickmatch.minigame.MatchGameClient;
import com.readutf.quickmatch.minigame.games.MatchSupplier;
import com.readutf.quickmatch.shared.JoinIntent;
import com.readutf.quickmatch.shared.intent.IntentService;
import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.UUID;

public class QuickMatchGameAPI {

    private static QuickMatchGameAPI instance;

    private final MatchGameClient matchGameClient;
    private final LiveProfileManager liveProfileManager;
    private final IntentService intentService;

    public QuickMatchGameAPI(MatchGameClient matchGameClient, LiveProfileManager liveProfileManager, IntentService intentService) {
        instance = this;
        this.matchGameClient = matchGameClient;
        this.liveProfileManager = liveProfileManager;
        this.intentService = intentService;
    }

    public @Nullable JoinIntent getJoinIntent(UUID playerId) {
        String gameId = liveProfileManager.getProfile(playerId).map(LiveProfile::getJoinIntent).filter(s -> !s.equalsIgnoreCase("")).orElse(null);
        if (gameId == null) return null;

        return RequestHelper.get(intentService.getIntent(gameId));
    }

    public void setMatchSupplier(MatchSupplier matchSupplier) {
        matchGameClient.setMatchSupplier(matchSupplier);
    }


    public static QuickMatchGameAPI getInstance() throws Exception {
        if (instance == null) throw new Exception("This server is not a game server.");
        return instance;
    }

}
