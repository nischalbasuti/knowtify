package com.hydratech19gmail.notify;

/*
 * Created by Jaelse on 09-08-2016.
 */
public class Notification {

    private String name;
    private String subject;
    private String content;
    private String timeStamp;
    /*
    private String broadcastName;
    private String broadcasterName;
    private String broadcasterId;
*/
    public Notification(){

    }

    public Notification(String name,String subject,String content,String timeStamp) {
        this.name = name;
        this.subject = subject;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
}
