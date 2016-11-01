package com.hydratech19gmail.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;


/*
 * Created by Jaelse on 01-08-2016.
 */
public class CustomAdapter extends ArrayAdapter<Notification> {
    LayoutInflater inflater;

    ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView notificationName;
        public TextView broadcastName;
        public TextView contentText;
        public ImageView dropDownImage;
        public ImageView queryImage;
        public ImageView attachmentImage;
        public ImageView alarmImage;
        public LinearLayout subjectLinearLayout;
        public TextView timeStamp;

        //metadata
        TextView userKey;
        TextView broadcastKey;
        TextView notificationKey;
    }

    public CustomAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.home_fragment_row,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.notificationName = (TextView)convertView.findViewById(R.id.notification_name);
            mViewHolder.broadcastName = (TextView)convertView.findViewById(R.id.broadcast_name);
            mViewHolder.contentText = (TextView)convertView.findViewById(R.id.content);
            mViewHolder.dropDownImage = (ImageView)convertView.findViewById(R.id.dropDownMenu);
            mViewHolder.queryImage = (ImageView)convertView.findViewById(R.id.ask_question);
            mViewHolder.attachmentImage = (ImageView)convertView.findViewById(R.id.attachment);
            mViewHolder.subjectLinearLayout = (LinearLayout)convertView.findViewById(R.id.contentLayout);
            mViewHolder.alarmImage = (ImageView) convertView.findViewById(R.id.renimder);
            mViewHolder.timeStamp = (TextView)convertView.findViewById(R.id.time);


            mViewHolder.userKey = ((TextView)convertView.findViewById(R.id.userKey));
            mViewHolder.broadcastKey = ((TextView)convertView.findViewById(R.id.broadcastKey));
            mViewHolder.notificationKey = ((TextView)convertView.findViewById(R.id.notificationKey));

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

            Log.d("CustomAdapter","getView | not null");
        }


        final Notification n = getItem(position);

        mViewHolder.notificationName.setText(n.getName());
        mViewHolder.broadcastName.setText(n.getBroadcast());
        mViewHolder.contentText.setText(n.getSubject());
        mViewHolder.timeStamp.setText(DateConvert.timeStampToDate(Long.parseLong(n.getTimeStamp())*1000));

        //metadata
        mViewHolder.userKey.setText(n.getUserKey());
        mViewHolder.broadcastKey.setText(n.getBroadcastKey());
        mViewHolder.notificationKey.setText(n.getNotificationKey());

        //setting dropdown
        mViewHolder.dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"Delete", "Mark as Read"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                            UpdateNotificationsTask updateNotificationsTask = new UpdateNotificationsTask(getContext(),n);
                            updateNotificationsTask.execute("delete_a_notification");

                            Log.d("NOTIFICAIONS","a"+n.getTimeStamp());
                            NOTIFICATIONS.remove(n);

                            CustomAdapter.this.notifyDataSetChanged();
                        } else if (which == 1) {
                            Toast.makeText(getContext(), "Mark as Read", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });
        mViewHolder.queryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),QueryActivity.class);
                intent.putExtra("user_key",n.getUserKey());
                intent.putExtra("broadcast_key",n.getBroadcastKey());
                intent.putExtra("notification_key",n.getNotificationKey());
                intent.putExtra("notification_name",n.getName());
                intent.putExtra("notification_content",n.getContent());

                getContext().startActivity(intent);
            }
        });

        mViewHolder.attachmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog attachmentDialog = new Dialog(getContext());
                attachmentDialog.setTitle("Attachments");
                attachmentDialog.setContentView(R.layout.dialog_attachment);

                View rootView = inflater.inflate(R.layout.dialog_attachment,null);
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                ListAdapter listAdapter = new CustomDownloadDialogAdapter(getContext(),NOTIFICATIONS,storageReference);
                ListView downloadListView = (ListView)rootView.findViewById(R.id.downloadListView);
                downloadListView.setAdapter(listAdapter);

                attachmentDialog.show();
                attachmentDialog.setContentView(rootView);

                Button closeB = (Button)attachmentDialog.findViewById(R.id.closeDialog);
                closeB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListIterator<FileDownloadTask> storageReferenceListIterator = storageReference.getActiveDownloadTasks().listIterator();
                        while (storageReferenceListIterator.hasNext()){
                            storageReferenceListIterator.next().cancel();
                        }
                        attachmentDialog.cancel();
                    }
                });
            }
        });


        mViewHolder.subjectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),n.getSubject().toString(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(),WholeNotificationActivity.class);

                intent.putExtra("broadcastName", n.getBroadcast());
                intent.putExtra("notificationName", n.getName());
                intent.putExtra("notificationSubject",n.getSubject());
                intent.putExtra("notificationContent",n.getContent());
                intent.putExtra("notificationTimestamp",n.getTimeStamp());

                ((Activity)getContext()).startActivityForResult(intent,1);
            }
        });

        mViewHolder.alarmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                CalendarEventMaker calendarEventMaker = new CalendarEventMaker(getContext(),
                        calendar.getTimeInMillis(),calendar.getTimeInMillis()+60*60*1000,"Event Making Test");
                calendarEventMaker.makeEvent();
                Toast.makeText(getContext(),"Event Setup",Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }
}
