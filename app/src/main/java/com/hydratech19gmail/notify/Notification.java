package com.hydratech19gmail.notify;

/*
 * Created by Jaelse on 09-08-2016.
 */
public class Notification {

    private String broadcast;
    private String name;
    private String timeStamp;
    private String subject;
    private String content;

    public Notification(){

    }

    public Notification(String broadcast,String name,String subject,String content,String timeStamp) {
        this.broadcast = broadcast;
        this.name = name;
        this.subject = subject;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    //getters and setters


    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
