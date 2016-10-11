package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;
import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

/**
 * Created by zappereton on 9/10/16.
 */

public class RetrieveNotificationsTask extends AsyncTask<String,Notification,String> {
    Context ctx;
    private final String TAG = "NotificationUpdateTask";
    Notification n;
    DatabaseReference notificationRef;
    RetrieveNotificationsTask(Context ctx) {
        this.ctx = ctx;
    }

    RetrieveNotificationsTask(Context ctx, Notification n){
        this.ctx = ctx;
        this.n = n;
    }
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {

        synchronized (this) {
            String method = params[0];

            if (method.equals("get_all_notifications")) {
                Log.d(TAG,"get_all_notification called");
                //get all the notification from the database if any.
                DatabaseOperations dop = new DatabaseOperations(ctx);
                Cursor allNotifications = dop.getAllNotifications(dop);
                while (allNotifications.moveToNext()) {

                    String broadcastName = allNotifications.getString(allNotifications.getColumnIndex(TableData.TableInfo.BROADCAST_NAME));
                    String notificationName = allNotifications.getString(allNotifications.getColumnIndex(TableData.TableInfo.NOTIFICATION_NAME));
                    String notificationsSubject = allNotifications.getString(allNotifications.getColumnIndex(TableData.TableInfo.NOTIFICATION_SUBJECT));
                    String notificationContent = allNotifications.getString(allNotifications.getColumnIndex(TableData.TableInfo.NOTIFICATION_CONTENT));

                    Notification n = new Notification(broadcastName, notificationName, notificationsSubject, notificationContent, null);

                    publishProgress(n);
                }

                //listen for new notifications
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("myprefs", MODE_PRIVATE);
                String prefUserKey = sharedPreferences.getString("user_key", "user key doesnt exits");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference notificationRef = ref.child("users")
                        .child(prefUserKey)
                        .child("newNotification");

                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot notification : dataSnapshot.getChildren()) {
                            try {
                                Log.d("child notifications", notification.toString());
                        /*add these notifications to the local database*/
                                DatabaseOperations dop = new DatabaseOperations(ctx);
                                Notification newNotification = notification.getValue(Notification.class);
                                dop.putNotification(dop, newNotification);
                                NOTIFICATIONS.addFirst(newNotification);
                                DatabaseReference oldNotificationRef = notificationRef.child(notification.getKey());
                                oldNotificationRef.removeValue();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return "get_all_notifications";
            }
            if (method.equals("reinstalled")){
                final SharedPreferences sharedPreferences = ctx.getSharedPreferences("myprefs", MODE_PRIVATE);
                String prefUserKey = sharedPreferences.getString("user_key", "user key doesnt exits");

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference notificationRef = ref.child("users")
                        .child(prefUserKey)
                        .child("subscriptions");

                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot channel : dataSnapshot.getChildren()) {
                            try {

                                Subscriptions subscriptions = channel.getValue(Subscriptions.class);
                                String userName = subscriptions.getUserName();
                                String channelName = subscriptions.getChannelName();

                                DatabaseReference channelRef = ref.child("users")
                                                                    .child(userName)
                                                                    .child("broadcasts")
                                                                    .child(channelName);
                                channelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot notification : dataSnapshot.getChildren()){
                                            try{
                                                /*sort the notificatins according to their timestamp.
                                                add these notifications to the local database
                                                add these notifications to the NOTIFICATIONS*/

                                                /*
                                                DatabaseOperations dop = new DatabaseOperations(ctx);
                                                Notification newNotification = notification.getValue(Notification.class);
                                                dop.putNotification(dop, newNotification);
                                                NOTIFICATIONS.addFirst(newNotification); */
                                            }catch (Exception e){
                                                Log.d(TAG,e.getMessage());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return "reinstalled";
            }
            if(method.equals("signed_in_with_different_account")){
                //fire delete all the old data from the database.
                DatabaseOperations dop = new DatabaseOperations(ctx);
                dop.deleteAllNotification(dop);

                //get the data again from the remote database.
                final SharedPreferences sharedPreferences = ctx.getSharedPreferences("myprefs", MODE_PRIVATE);
                String prefUserKey = sharedPreferences.getString("user_key", "user key doesnt exits");

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference notificationRef = ref.child("users")
                        .child(prefUserKey)
                        .child("subscriptions");

                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot channel : dataSnapshot.getChildren()) {
                            try {

                                Subscriptions subscriptions = channel.getValue(Subscriptions.class);
                                String userName = subscriptions.getUserName();
                                String channelName = subscriptions.getChannelName();

                                DatabaseReference channelRef = ref.child("users")
                                        .child(userName)
                                        .child("broadcasts")
                                        .child(channelName);
                                channelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot notification : dataSnapshot.getChildren()){
                                            try{
                                                /*sort the notificatins according to their timestamp.
                                                add these notifications to the local database
                                                add these notifications to the NOTIFICATIONS*/

                                                /*
                                                DatabaseOperations dop = new DatabaseOperations(ctx);
                                                Notification newNotification = notification.getValue(Notification.class);
                                                dop.putNotification(dop, newNotification);
                                                NOTIFICATIONS.addFirst(newNotification); */
                                            }catch (Exception e){
                                                Log.d(TAG,e.getMessage());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return "reinstalled";
            }
            if(method.equals("delete_a_notification")){
                if(n != null){
                    Log.d("delete_a_notification","entered");
                    String broadcastName = n.getBroadcast();
                    String notificationName = n.getName();
                    String notificationSubject = n.getSubject();

                    DatabaseOperations dop = new DatabaseOperations(ctx);
                    dop.deleteNotification(dop,broadcastName,notificationName,notificationSubject);
                }
                return "delete_a_notifcation";
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Notification... values) {
        NOTIFICATIONS.addFirst(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
    }
}
