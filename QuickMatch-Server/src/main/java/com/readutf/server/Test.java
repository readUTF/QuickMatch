package com.readutf.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

        MongoDatabase database = mongoClient.getDatabase("test");

        if(!database.listCollectionNames().into(new HashSet<>()).contains("queue-activity")) {
            TimeSeriesOptions timeSeriesOptions = new TimeSeriesOptions("timestamp");
            timeSeriesOptions.granularity(TimeSeriesGranularity.MINUTES);
            CreateCollectionOptions collectionOptions = new CreateCollectionOptions().timeSeriesOptions(timeSeriesOptions);
            database.createCollection("queue-activity", collectionOptions);
        }
        MongoCollection<Document> collection = database.getCollection("queue-activity");

//        collection.insertMany(Arrays.asList(
//                new Document("metadata", "test1").append("timestamp", new Date()).append("count", ThreadLocalRandom.current().nextInt(0, 1000)),
//                new Document("metadata", "test1").append("timestamp", new Date()).append("count", ThreadLocalRandom.current().nextInt(0, 1000)),
//                new Document("metadata", "test1").append("timestamp", new Date()).append("count", ThreadLocalRandom.current().nextInt(0, 1000)),
//                new Document("metadata", "test1").append("timestamp", new Date()).append("count", ThreadLocalRandom.current().nextInt(0, 1000))
//        ));

        collection.aggregate(
                List.of(
                        Aggregates.match(Filters.eq("metadata", "test1")),
                        Aggregates.match(Filters.gte("timestamp", new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))))
                )
        ).forEach(document -> System.out.println(document.toJson()));


    }

    public Date getISODate(long time) {
        return new Date(time);
    }

}
