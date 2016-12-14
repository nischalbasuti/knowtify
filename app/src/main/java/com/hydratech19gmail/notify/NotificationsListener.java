package com.hydratech19gmail.notify;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by zappereton on 31/10/16.
 */
public class NotificationsListener {
    String uid;
    String channelKey;
    ListAdapter listAdapter;
    Context ctx;

    NotificationsListener(ListAdapter listAdapter, Context ctx, String uid, String channelKey){
        this.uid = uid;
        this.channelKey = channelKey;

        this.ctx = ctx;


        this.listAdapter = listAdapter;

    }


}
