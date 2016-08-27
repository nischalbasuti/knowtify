package com.hydratech19gmail.notify;

import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jaelse on 27-08-2016.
 */
public class FirebaseInstaceIDService extends FirebaseInstanceIdService{
    public static final String TAG = "TOKEN";

    @Override
    public void onTokenRefresh() {


        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,token);
        registerToken(token);
    }

    public void registerToken(final String token){
        final Firebase ref = new Firebase("https://notify-1384.firebaseio.com/notifications/tokens");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ref.push().setValue("Jaelse",token);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Log.d(TAG,token);
    }
}
