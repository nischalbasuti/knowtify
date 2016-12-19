package com.hydratech19gmail.notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
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

        String name = remoteMessage.getData().get("title");
        String subject = remoteMessage.getData().get("body");
        String content = remoteMessage.getData().get("content");

        showNotification(name,subject,content);
        /*get the url for the new notification with the attachment url if any*/
    }

    private void showNotification(String name,String subject,String content) {

        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setVibrate(new long[]{250, 250, 250, 250, 250})
                .setContentTitle(name)
                .setContentText(subject)
                .setLights(Color.RED,3000,3000)
                .setSmallIcon(R.drawable.ic_launcher_knowtify)
                .setContentIntent(pendingIntent);
            //need to add a sound for the notification
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        int num = (int) System.currentTimeMillis();
        manager.notify(num,builder.build());
    }
}
