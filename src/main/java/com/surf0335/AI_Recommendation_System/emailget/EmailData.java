package com.surf0335.AI_Recommendation_System.emailget;

public class EmailData {

    private String subject;
    private String sender;
    private String date;
    private String content;

    public EmailData(String subject, String sender, String date, String content) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.content = content;
    }

    // Getters and Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
