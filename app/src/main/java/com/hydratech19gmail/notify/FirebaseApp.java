package com.hydratech19gmail.notify;

import com.firebase.client.Firebase;

/**
 * Created by Jaelse on 09-08-2016.
 */
public class FirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
