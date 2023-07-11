package com.readutf.quickmatch.minigame.api;

import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.UUID;

public class QuickMatchGameAPI {

    private @Getter static QuickMatchGameAPI instance;

    private final LiveProfileManager liveProfileManager;

    public QuickMatchGameAPI(LiveProfileManager liveProfileManager) {
        instance = this;
        this.liveProfileManager = liveProfileManager;
    }

    public @Nullable String getJoinIntent(UUID playerId) {
        return liveProfileManager.getProfile(playerId).map(LiveProfile::getJoinIntent).filter(s -> !s.equalsIgnoreCase("")).orElse(null);
    }

    public static QuickMatchGameAPI getInstance() throws Exception {
        if(instance == null) throw new Exception("This server is not a game server.");
        return instance;
    }

}
