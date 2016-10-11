package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

/**
 * Created by zappereton on 11/10/16.
 */

public class WholeNotificationActivity extends AppCompatActivity implements View.OnClickListener{

    String broadcastName;
    String notificationName;
    String notificationSubject;
    String notificationContent;

    TextView broadcastNameTV;
    TextView notificationNameTV;
    TextView notificationsSubjectTV;
    TextView notificationContentTV;

    ImageView dropDownImage;
    ImageView queryImage;
    ImageView attachmentImage;

    final String TAG = "WholeNotification";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.whole_notification);
        //getting data from previous activity
        broadcastName = getIntent().getExtras().getString("broadcastName");
        notificationName = getIntent().getExtras().getString("notificationName");
        notificationSubject = getIntent().getExtras().getString("notificationSubject");
        notificationContent = getIntent().getExtras().getString("notificationContent");

        broadcastNameTV = (TextView)findViewById(R.id.wbroadcastName);
        notificationNameTV = (TextView)findViewById(R.id.wNotificationName);
        notificationsSubjectTV = (TextView)findViewById(R.id.wSubject);
        notificationContentTV = (TextView)findViewById(R.id.wContent);

        broadcastNameTV.setText(broadcastName);
        notificationNameTV.setText(notificationName);
        notificationsSubjectTV.setText(notificationSubject);
        notificationContentTV.setText(notificationContent);

        dropDownImage = (ImageView)findViewById(R.id.wdropDownMenu);
        queryImage = (ImageView)findViewById(R.id.wask_question);
        attachmentImage = (ImageView)findViewById(R.id.wattachment);

        dropDownImage.setOnClickListener(this);
        queryImage.setOnClickListener(this);
        attachmentImage.setOnClickListener(this);

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(broadcastName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_attachment,null);

        switch (v.getId()){
            case R.id.wdropDownMenu:

                String[] options = {"Delete","Mark as Read"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WholeNotificationActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Toast.makeText(WholeNotificationActivity.this,"Delete",Toast.LENGTH_SHORT).show();
                            Notification n = new Notification(broadcastName,notificationName,notificationSubject,notificationContent,null);

                            UpdateNotificationsTask updateNotificationsTask = new UpdateNotificationsTask(WholeNotificationActivity.this,n);
                            updateNotificationsTask.execute("delete_a_notification");

                            Log.d("SizeB", String.valueOf(NOTIFICATIONS.size()));
                            NOTIFICATIONS.remove(n);

                            onBackPressed();
                        }
                        else if(which == 0){
                            Toast.makeText(WholeNotificationActivity.this,"Mark as Read",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
                break;
            case R.id.wask_question:
                Toast.makeText(WholeNotificationActivity.this,"uouououo",Toast.LENGTH_LONG).show();
                break;
            case R.id.wattachment:
                Log.d(TAG,"wattachment");
                Toast.makeText(WholeNotificationActivity.this,"asdfsdf",Toast.LENGTH_SHORT).show();
                final Dialog attachmentDialog = new Dialog(WholeNotificationActivity.this);
                attachmentDialog.setTitle("Attachments");
                attachmentDialog.setContentView(R.layout.dialog_attachment);

                ListAdapter listAdapter = new CustomDownloadDialogAdapter(WholeNotificationActivity.this,NOTIFICATIONS);
                ListView attachmentListView = (ListView)rootView.findViewById(R.id.downloadListView);
                attachmentListView.setAdapter(listAdapter);

                attachmentDialog.show();
                attachmentDialog.setContentView(rootView);

                Button closeB = (Button)attachmentDialog.findViewById(R.id.closeDialog);
                closeB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attachmentDialog.cancel();
                    }
                });
                break;
        }
    }
}