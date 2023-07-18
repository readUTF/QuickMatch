package com.readutf.server.analytics;

import com.mongodb.client.MongoDatabase;
import com.readutf.server.analytics.impl.QueueActivity;
import lombok.Getter;

@Getter
public class AnalyticsManager {

    private final QueueActivity queueActivityTracker;

    public AnalyticsManager(MongoDatabase database) {
        this.queueActivityTracker = new QueueActivity("queue_activity", database);
    }

}
