package com.readutf.server.analytics;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesGranularity;
import com.mongodb.client.model.TimeSeriesOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public abstract class Analytic {

    MongoCollection<Document> collection;

    public Analytic(String id, MongoDatabase mongoDatabase) {
        this.collection = getCollection(mongoDatabase);
    }

    public void addActivity(String metadata, int count) {
        System.out.println("Adding activity for " + metadata + " with count " + count);
        collection.insertOne(new Document("metadata", metadata).append("timestamp", new Date()).append("count", count));
    }

    public MongoCollection<Document> getCollection(MongoDatabase database) {
        if(!database.listCollectionNames().into(new ArrayList<>()).contains("queue-activity")) {
            TimeSeriesOptions timeSeriesOptions = new TimeSeriesOptions("timestamp");
            timeSeriesOptions.granularity(TimeSeriesGranularity.MINUTES);
            CreateCollectionOptions collectionOptions = new CreateCollectionOptions().timeSeriesOptions(timeSeriesOptions);
            database.createCollection("queue-activity", collectionOptions);
        }
        return database.getCollection("queue-activity");
    }

}
