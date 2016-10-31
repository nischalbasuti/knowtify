package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;
import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

/**
 * Created by zappereton on 9/10/16.
 */

public class UpdateNotificationsTask extends AsyncTask<String,Notification,String> {
    Context ctx;
    private final String TAG = "NotificationUpdateTask";
    Notification n;
    ListAdapter listAdapter;

    UpdateNotificationsTask(Context ctx, Notification n, ListAdapter listAdapter){
        this.ctx = ctx;
        this.n = n;
        this.listAdapter = listAdapter;
    }
    UpdateNotificationsTask(Context ctx, Notification n){
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
            if(method.equals("addAndSort")){

                NOTIFICATIONS.addFirst(n);
                publishProgress(n);
                /*
                int size = NOTIFICATIONS.size();
                Long newTimeStamp = Long.parseLong(n.getTimeStamp());

                if(size <= 0){
                    NOTIFICATIONS.add(n);
                    publishProgress(n);
                }
                else if(size > 0){
                    for (int i = size-1; i >= 0; i--) {
                        Long currentTimeStamp = Long.parseLong(NOTIFICATIONS.get(i).getTimeStamp());
                        if(newTimeStamp < currentTimeStamp && ((i-1) != 0)){
                            //when the new node is less than the current node
                            //and there is a node below the current node.
                            Log.d("case","1");
                            continue;
                        }
                        else if(newTimeStamp < currentTimeStamp && ((i-1) <= 0)){
                            //when the new node is less than the current node
                            //and there is no node below the current node.
                            NOTIFICATIONS.addLast(n);
                            publishProgress(n);
                            Log.d("case","2");
                        }
                        else if(newTimeStamp > currentTimeStamp){
                            if((i+1) >= size){
                                //when the new node is greater than the current node
                                //and there is a node above the current node.
                                NOTIFICATIONS.add(i,n);
                                publishProgress(n);
                                Log.d("case","3");
                            }
                            if(i+1 < size){
                                //when the new node is greater than the current node
                                //and there is no node above the current node.
                                NOTIFICATIONS.addFirst(n);
                                publishProgress(n);
                                Log.d("case","4");
                            }
                        }
                    }
                }*/
                return "addAndSort";
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
        ((CustomAdapter)listAdapter).notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(String s) {
    }
}
