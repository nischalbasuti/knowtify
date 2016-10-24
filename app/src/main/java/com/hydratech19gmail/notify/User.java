package com.hydratech19gmail.notify;

/**
 * Created by nischal on 24/10/16.
 */

public class User {
    private String uid;
    private String token;

    public User(){}

    public User(String uid, String token) {
        this.uid = uid;
        this.token = token;
    }

    //setters and getters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
