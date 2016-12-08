package com.hydratech19gmail.notify;

/**
 * Created by nischal on 8/12/16.
 */

public class Answer {
    private String timeStamp;
    private String content;
    private String userKey;

    private String notificationKey;
    private String broadcastUserKey;
    private String broadcastKey;

    private String answerKey;

    public Answer(){}

    public Answer(String timeStamp, String content, String userKey, String notificationKey, String broadcastUserKey, String broadcastKey){
        this.timeStamp = timeStamp;
        this.content = content;
        this.userKey = userKey;
        this.notificationKey = notificationKey;
        this.broadcastUserKey = broadcastUserKey;
        this.broadcastKey = broadcastKey;
    }

    public String getBroadcastKey() {
        return broadcastKey;
    }

    public void setBroadcastKey(String broadcastKey) {
        this.broadcastKey = broadcastKey;
    }

    public String getBroadcastUserKey() {
        return broadcastUserKey;
    }

    public void setBroadcastUserKey(String broadcastUserKey) {
        this.broadcastUserKey = broadcastUserKey;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
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

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
}
