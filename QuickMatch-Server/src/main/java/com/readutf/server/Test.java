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



    public Date getISODate(long time) {
        return new Date(time);
    }

}
