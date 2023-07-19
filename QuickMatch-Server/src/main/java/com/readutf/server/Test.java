package com.readutf.server;

import com.mongodb.client.MongoClients;
import com.readutf.server.analytics.impl.QueueActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Test {

    private final char square = '⬛';

    public static void main(String[] args) {




        QueueActivity queueActivity = new QueueActivity("queue_activity", MongoClients.create("mongodb://localhost:27017").getDatabase("QuickMatch"));

//        queueActivity.addActivity("NoDebuff", System.currentTimeMillis(), 1);
        for (int i = 0; i < 24; i++) {
            queueActivity.addActivity("NoDebuff", System.currentTimeMillis() - TimeUnit.HOURS.toMillis(i), ThreadLocalRandom.current().nextInt(0, 400));
        }

//        System.out.println(queueActivity.getAverageHourlyActivity());


    }

    public static List<String> generateGraph(List<Integer> values, int max) {
        int yIncrement = max / 8;

        List<String> lines = new ArrayList<>();

        for (int i = 8; i > 0; i--) {
            StringBuilder line = new StringBuilder();
            for (int size = values.size() - 1; size > Math.max(0, values.size() - 34) - 1; size--) {
                Integer current = values.get(size);

                if (current >= (yIncrement * i)) {
                    line.append("⬛");
                } else {
                    line.append("⬜");
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }

    public Date getISODate(long time) {
        return new Date(time);
    }

}
