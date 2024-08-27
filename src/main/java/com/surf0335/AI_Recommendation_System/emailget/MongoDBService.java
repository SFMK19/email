package com.surf0335.AI_Recommendation_System.emailget;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MongoDBService {

    private MongoCollection<Document> collection;

    public MongoDBService(@Value("${mongodb.uri}") String uri,
                          @Value("${mongodb.database}") String databaseName,
                          @Value("${mongodb.collection}") String collectionName) {
        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase(databaseName);
        this.collection = database.getCollection(collectionName);
    }

    public List<EmailData> findAllEmails() {
        List<EmailData> emails = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Document document : collection.find()) {
            String subject = document.getString("subject");
            String sender = document.getString("sender");
            String content = document.getString("content");

            Object dateObj = document.get("date");
            String dateString;

            if (dateObj instanceof Date) {
                dateString = dateFormat.format((Date) dateObj);
            } else if (dateObj instanceof String) {
                try {
                    Date date = dateFormat.parse((String) dateObj);
                    dateString = dateFormat.format(date);
                } catch (ParseException e) {
                    dateString = (String) dateObj;
                }
            } else {
                dateString = "N/A";
            }

            emails.add(new EmailData(subject, sender, dateString, content));
        }
        return emails;
    }

    public void saveEmail(String subject, String sender, String date, String content) {
        Document doc = new Document("subject", subject)
                .append("sender", sender)
                .append("date", date)
                .append("content", content)
                .append("fetched_at", new Date());

        collection.insertOne(doc);
    }
}
