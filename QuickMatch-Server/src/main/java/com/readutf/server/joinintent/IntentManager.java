package com.readutf.server.joinintent;

import com.readutf.quickmatch.shared.JoinIntent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class IntentManager {

    private final Map<String, JoinIntent> joinIntents;

    public IntentManager() {
        this.joinIntents = new HashMap<>();
    }

    public @Nullable JoinIntent getIntentById(String id) {
        return joinIntents.get(id);
    }

    public void saveIntent(JoinIntent joinIntent) {
        joinIntents.put(joinIntent.getGameId(), joinIntent);
    }

}
