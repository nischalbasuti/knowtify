package com.hydratech19gmail.notify;

/**
 * Created by zappereton on 10/10/16.
 */
public class Subscription {

    private String userName;
    private String channelName;
    private String subscribersKey;

    public Subscription(){
    }

    Subscription(String userName, String channelName, String subscribersKey){
        this.userName = userName;
        this.channelName = channelName;
        this.subscribersKey = subscribersKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSubscribersKey() {
        return subscribersKey;
    }

    public void setSubscribersKey(String subscribersKey) {
        this.subscribersKey = subscribersKey;
    }

}
