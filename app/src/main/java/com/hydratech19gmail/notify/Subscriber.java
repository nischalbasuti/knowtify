package com.hydratech19gmail.notify;

/**
 * Created by nischal on 21/10/16.
 */

public class Subscriber {
    private String subscriberKey;
    private String token;

    public Subscriber() {

    }

    public Subscriber(String subscriberKey, String token) {
        this.subscriberKey = subscriberKey;
        this.token = token;
    }

    //getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubscriberKey() {
        return subscriberKey;
    }

    public void setSubscriberKey(String subscriberKey) {
        this.subscriberKey = subscriberKey;
    }
}
