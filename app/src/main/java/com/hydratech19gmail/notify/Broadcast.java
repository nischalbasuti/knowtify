package com.hydratech19gmail.notify;

/*
 * Created by nischal on 18/8/16.
 */
public class Broadcast {

    private String name;
    private String info;
    private String userId;
    private String privacy;

    public Broadcast() {}

    //getters and setters

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
