package com.readutf.server.analytics.impl;

import com.mongodb.client.MongoDatabase;
import com.readutf.server.analytics.Analytic;
import com.readutf.server.queue.Queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueActivity extends Analytic {

    private final Map<String, Integer> queueActivity;

    private Long lastActivity = System.currentTimeMillis();

    public QueueActivity(String id, MongoDatabase mongoDatabase) {
        super(id, mongoDatabase);
        this.queueActivity = new HashMap<>();
    }

    public void onJoin(Queue queue) {
        long sinceLast = System.currentTimeMillis() - lastActivity;
        System.out.println(sinceLast);
        System.out.println(queueActivity);
        if(sinceLast > 30_000) {
            queueActivity.forEach(this::addActivity);
            queueActivity.clear();
        }
        queueActivity.merge(queue.getQueueType().getId(), 1, Integer::sum);
    }


}
