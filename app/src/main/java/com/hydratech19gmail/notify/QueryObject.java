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
}
