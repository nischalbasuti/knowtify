package com.hydratech19gmail.notify;

/**
 * Created by zappereton on 10/10/16.
 */
public class Subscriptions {
    String userName;
    String channelName;

    Subscriptions(String userName, String channelName){
        this.userName = userName;
        this.channelName = channelName;

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

}
