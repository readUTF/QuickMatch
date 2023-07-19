package com.readutf.server.analytics;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Analytic {

    public final MongoCollection<Document> collection;

    public Analytic(String id, MongoDatabase mongoDatabase) {
        this.collection = getCollection(id, mongoDatabase);
    }

    public void addActivity(String subId, double count) {
        System.out.println("Adding activity for " + subId + " with count " + count);
        for (int i = 0; i < 20; i++) {
            collection.insertOne(new Document("metadata", subId).append("timestamp", new Date()).append("count", count));
        }
    }

    public void addActivity(String subId, long timeStamp, double count) {
        System.out.println("Adding activity for " + subId + " with count " + count);

        collection.insertOne(new Document("metadata", subId).append("timestamp", new Date(timeStamp)).append("count", count));

    }


    public MongoCollection<Document> getCollection(String id, MongoDatabase database) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(id)) {
            TimeSeriesOptions timeSeriesOptions = new TimeSeriesOptions("timestamp");
            timeSeriesOptions.granularity(TimeSeriesGranularity.MINUTES);
            CreateCollectionOptions collectionOptions = new CreateCollectionOptions().timeSeriesOptions(timeSeriesOptions);
            database.createCollection(id, collectionOptions);
        }
        return database.getCollection(id);
    }

}
