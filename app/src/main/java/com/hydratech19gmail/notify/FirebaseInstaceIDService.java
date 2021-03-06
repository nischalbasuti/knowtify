package com.hydratech19gmail.notify;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*
 * Created by Jaelse on 27-08-2016.
 */
public class FirebaseInstaceIDService extends FirebaseInstanceIdService{
    public static final String TAG = "TOKEN";
    static String TOKEN;

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,token);
        setToken(token);
        SignupActivity.getToken(token);
        registerToken(token);

        //writting key value pair
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("device_token", token);
        editor.commit();
    }


    public static void setToken(String token){
        TOKEN = token;
    }
    public void registerToken(final String token){
       /* final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference tokensRef = ref.child("tokens");

        tokensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                tokensRef.push().setValue("Jaelse",token);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
