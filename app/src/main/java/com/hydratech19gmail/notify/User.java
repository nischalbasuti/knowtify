package com.hydratech19gmail.notify;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * Created by nischal on 24/10/16.
 */

public class User {
    private String uid;
    private String token;
    private String emailId;
    private String username;
    private String photoUrl;

    public User(){}

    public User(String uid, String token, String emailId, String username, String photoUrl) {
        this.uid = uid;
        this.token = token;
        this.emailId = emailId;
        this.username = username;
        this.photoUrl = photoUrl;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailid) {
        this.emailId = emailid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
