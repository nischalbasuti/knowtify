package com.hydratech19gmail.notify;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jaelse on 09-08-2016.
 */
public class FirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
