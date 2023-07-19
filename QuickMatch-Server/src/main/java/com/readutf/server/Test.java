package com.readutf.server;

import com.image.charts.ImageCharts;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.readutf.quickmatch.shared.chart.LineChart;
import com.readutf.server.analytics.impl.QueueActivity;
import org.bson.Document;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
