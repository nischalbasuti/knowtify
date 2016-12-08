package com.hydratech19gmail.notify;

/*
 * Created by nischal on 15/9/16.
 */
public class QueryObject {
    private String querySubject;
    private String queryContent;
    private String queryUserKey;
    private String timeStamp;
    private int rating;


    private String notificationKey;
    private String broadcastUserKey;
    private String broadcastKey;
    private String queryKey;

    public QueryObject(){}

    //getters and setters
    public String getQuerySubject() {
        return querySubject;
    }

    public void setQuerySubject(String querySubject) {
        this.querySubject = querySubject;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getQueryUserKey() {
        return queryUserKey;
    }

    public void setQueryUserKey(String queryUserKey) {
        this.queryUserKey = queryUserKey;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getBroadcastUserKey() {
        return broadcastUserKey;
    }

    public void setBroadcastUserKey(String broadcastUserKey) {
        this.broadcastUserKey = broadcastUserKey;
    }

    public String getBroadcastKey() {
        return broadcastKey;
    }

    public void setBroadcastKey(String broadcastKey) {
        this.broadcastKey = broadcastKey;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }
}
