package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

/*
 * Created by zappereton on 11/10/16.
 */

public class WholeNotificationActivity extends AppCompatActivity implements View.OnClickListener{
    public static Boolean CHECK = false;

    String broadcastName;
    String notificationName;
    String notificationSubject;
    String notificationContent;
    String notificationTimestamp;

    //TODO: 8/12/16  get data from getIntent.getExtras(); in onCreate()
    String broadcastUserKey;
    String broadcastKey;
    String notificationKey;

    TextView broadcastNameTV;
    TextView notificationNameTV;
    TextView notificationsSubjectTV;
    TextView notificationContentTV;
    TextView notificationTimestampTV;

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
        notificationTimestamp = getIntent().getExtras().getString("notificationTimestamp");

        // TODO: 8/12/16 still need to get info from where this Activity is being started from
        broadcastUserKey = getIntent().getExtras().getString("broadcastUserKey");
        broadcastKey = getIntent().getExtras().getString("broadcastKey");
        notificationKey = getIntent().getExtras().getString("notificationKey");

        broadcastNameTV = (TextView)findViewById(R.id.wbroadcastName);
        notificationNameTV = (TextView)findViewById(R.id.wNotificationName);
        notificationsSubjectTV = (TextView)findViewById(R.id.wSubject);
        notificationContentTV = (TextView)findViewById(R.id.wContent);
        notificationTimestampTV = (TextView)findViewById(R.id.wtime);

        broadcastNameTV.setText(broadcastName);
        notificationNameTV.setText(notificationName);
        notificationsSubjectTV.setText(notificationSubject);
        notificationContentTV.setText(notificationContent);
        notificationTimestampTV.setText(DateConvert.timeStampToDate(Long.parseLong(notificationTimestamp)*1000));

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
        final LayoutInflater inflater = getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.dialog_attachment,null);

        switch (v.getId()){
            case R.id.wdropDownMenu:

                String[] options = {"Delete","Mark as Read"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WholeNotificationActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Toast.makeText(WholeNotificationActivity.this,"Delete",Toast.LENGTH_SHORT).show();
                        //TODO add timestamp and broadcastKey
                            //    Notification n = new Notification(broadcastName,notificationName,notificationSubject,notificationContent,null, userId, broadcastKey);
                            Notification n = new Notification();
                            n.setBroadcast(broadcastName);
                            n.setName(notificationName);
                            n.setSubject(notificationSubject);
                            n.setContent(notificationContent);
                            n.setTimeStamp(null);
                            UpdateNotificationsTask updateNotificationsTask = new UpdateNotificationsTask(WholeNotificationActivity.this,n);
                            updateNotificationsTask.execute("delete_a_notification");

                            CHECK = false;
                            for(Notification h : NOTIFICATIONS) {
                                if(broadcastName.equals(h.getBroadcast()) &&
                                        notificationName.equals(h.getName()) &&
                                        notificationSubject.equals(h.getSubject()) &&
                                        notificationContent.equals(h.getContent())){

                                    CHECK = NOTIFICATIONS.remove(h);
                                    Log.d("check",Boolean.toString(CHECK));
                                    break;
                                }
                            }

                            onBackPressed();
                        }
                    }
                });
                dialogBuilder.show();
                break;
            case R.id.wask_question:
                //open QueryActivity
                Intent intent = new Intent(this,QueryActivity.class);
                intent.putExtra("user_key",broadcastUserKey);// TODO: 8/12/16
                intent.putExtra("broadcast_key",broadcastKey);// TODO: 8/12/16
                intent.putExtra("notification_key",notificationKey);// TODO: 8/12/16
                intent.putExtra("notification_name",notificationName);
                intent.putExtra("notification_content",notificationContent);

                startActivity(intent);

                Toast.makeText(WholeNotificationActivity.this,"uouououo",Toast.LENGTH_LONG).show();

                break;
            case R.id.wattachment:
                Log.d(TAG,"wattachment");
                Toast.makeText(WholeNotificationActivity.this,"asdfsdf",Toast.LENGTH_SHORT).show();
                final Dialog attachmentDialog = new Dialog(WholeNotificationActivity.this);
                attachmentDialog.setTitle("Attachments");
                attachmentDialog.setContentView(R.layout.dialog_attachment);

                final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                ListAdapter listAdapter = new CustomDownloadDialogAdapter(WholeNotificationActivity.this,NOTIFICATIONS,storageReference);
                ListView attachmentListView = (ListView)rootView.findViewById(R.id.downloadListView);
                attachmentListView.setAdapter(listAdapter);

                attachmentDialog.show();
                attachmentDialog.setContentView(rootView);

                Button closeB = (Button)attachmentDialog.findViewById(R.id.closeDialog);
                closeB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (FileDownloadTask fileDownloadTask : storageReference.getActiveDownloadTasks()) {
                            fileDownloadTask.cancel();
                        }
                        attachmentDialog.cancel();
                    }
                });
                break;
        }
    }
}