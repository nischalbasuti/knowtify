package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
/*
 * Created by nischal on 25/8/16.
 */
public class NewNotificationDialog extends Dialog implements View.OnClickListener {

    final FirebaseUser mUser;

    private EditText notificationName;
    private EditText notificationSubject;
    private EditText notificationContent;

    public NewNotificationDialog(Context context, FirebaseUser user) {
        super(context);
        this.mUser = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_notification);

        notificationName = (EditText) findViewById(R.id.notification_name);
        notificationSubject = (EditText) findViewById(R.id.notification_subject);
        notificationContent = (EditText) findViewById(R.id.notification_content);

        Button uploadAttachButton = (Button) findViewById(R.id.upload_attachments_button);
        uploadAttachButton.setOnClickListener(this);

        Button newNotificationButton = (Button) findViewById(R.id.new_notification_button);
        newNotificationButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.new_notification_exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload_attachments_button:
                uploadAttachments();
                break;
            case R.id.new_notification_button:
                makeNewNotification();
                dismiss();
                break;
            case R.id.new_notification_exit_button:
                dismiss();
                break;
        }
    }

    private void makeNewNotification() {
        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/notifications/");

        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();

        Notification notification = new Notification(
                notificationName.getText().toString(),
                notificationSubject.getText().toString(),
                notificationContent.getText().toString(),
                timeStamp
        );
        ref.push().setValue(notification);
    }

    private void uploadAttachments() {
    }
}
