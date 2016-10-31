package com.hydratech19gmail.notify;

import android.content.Context;
import android.widget.ListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by zappereton on 31/10/16.
 */
public class NotificationsListener {
    String uid;
    String channelKey;
    ListAdapter listAdapter;
    DatabaseReference channelRef;
    Context ctx;
    NotificationsListener(ListAdapter listAdapter, Context ctx, String uid, String channelKey){
        this.uid = uid;
        this.channelKey = channelKey;

        this.ctx = ctx;

        channelRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(uid)
                .child("broadcasts")
                .child(channelKey)
                .child("notifications");
        this.listAdapter = listAdapter;
    }

    public void getNotifications() {

        channelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //only take the new notification.
                for(DataSnapshot notification : dataSnapshot.getChildren()) {
                    Notification n = notification.getValue(Notification.class);
                    UpdateNotificationsTask updateNotificationsTask = new UpdateNotificationsTask(ctx,n,listAdapter);
                    updateNotificationsTask.execute("addAndSort");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
