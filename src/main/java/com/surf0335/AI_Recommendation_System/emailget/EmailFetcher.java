package com.surf0335.AI_Recommendation_System.emailget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Properties;

@Service
public class EmailFetcher {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private String port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Autowired
    private MongoDBService mongoDBService;

    @Autowired
    private LogService logService;

    public void fetchEmails() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Fetch unread messages
            Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                saveEmailToDatabase(message);
            }

            logService.log("Successfully fetched " + messages.length + " emails.");

            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
            logService.log("Failed to fetch emails: " + e.getMessage());
        }
    }

    private void saveEmailToDatabase(Message message) {
        try {
            String subject = message.getSubject();
            String sender = ((InternetAddress) message.getFrom()[0]).getAddress();
            String date = message.getReceivedDate().toString();
            String content = getTextFromMessage(message);

            mongoDBService.saveEmail(subject, sender, date, content);

        } catch (Exception e) {
            e.printStackTrace();
            logService.log("Failed to save email: " + e.getMessage());
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                result.append(bodyPart.getContent().toString());
            }
            return result.toString();
        }
        return "";
    }
}
