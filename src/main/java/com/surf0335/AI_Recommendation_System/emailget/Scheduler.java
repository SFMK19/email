package com.surf0335.AI_Recommendation_System.emailget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private EmailFetcher emailFetcher;

    @Scheduled(cron = "${scheduler.cron}")
    public void run() {
        emailFetcher.fetchEmails();
    }
}
