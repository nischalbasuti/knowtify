package com.hydratech19gmail.notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Jaelse on 18-08-2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("remosteMessage",remoteMessage.getData().get("body").toString());
        //String name = remoteMessage.getData().get("title");
        //String subject = remoteMessage.getData().get("body");
        //String content = remoteMessage.getData().get("Content");
        //Log.d("title",name);
        //showNotification(name);
        /*get the url for the new notification with the attachment url if any*/
    }

    private void showNotification(String name) {

        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(name)
                .setSmallIcon(R.drawable.ic_tab_broadcast)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}
