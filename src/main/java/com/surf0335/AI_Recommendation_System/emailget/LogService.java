package com.surf0335.AI_Recommendation_System.emailget;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LogService {

    public void log(String message) {
        try (FileWriter fw = new FileWriter("fetch_log.txt", true)) {
            fw.write(LocalDateTime.now() + ": " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
