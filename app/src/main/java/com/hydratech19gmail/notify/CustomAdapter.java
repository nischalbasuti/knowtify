package com.hydratech19gmail.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;


/*
 * Created by Jaelse on 01-08-2016.
 */
public class CustomAdapter extends ArrayAdapter<Notification> {
    ArrayList<String> mDropDownList;

    LayoutInflater inflater;

    ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView broadcastTitle;
        //public ImageView broadcastThumbImage;
        public TextView broadcasterName;
        public TextView contentText;
        public ImageView dropDownImage;
        public ImageView queryImage;
        public ImageView attachmentImage;
        public ImageView alarmImage;
        public LinearLayout subjectLinearLayout;
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
            mViewHolder.broadcastTitle = (TextView)convertView.findViewById(R.id.broadcastTitle);
           // mViewHolder.broadcastThumbImage = (ImageView)convertView.findViewById(R.id.thumbnail_image);
            mViewHolder.broadcasterName = (TextView)convertView.findViewById(R.id.broadcasterName);
            mViewHolder.contentText = (TextView)convertView.findViewById(R.id.content);
            mViewHolder.dropDownImage = (ImageView)convertView.findViewById(R.id.dropDownMenu);
            mViewHolder.queryImage = (ImageView)convertView.findViewById(R.id.ask_question);
            mViewHolder.attachmentImage = (ImageView)convertView.findViewById(R.id.attachment);
            mViewHolder.subjectLinearLayout = (LinearLayout)convertView.findViewById(R.id.contentLayout);
            mViewHolder.alarmImage = (ImageView) convertView.findViewById(R.id.renimder);
            convertView.setTag(mViewHolder);

            Log.d("CustomAdapter","getView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

            Log.d("CustomAdapter","getView | not null");
        }


        final Notification n = getItem(position);

        mViewHolder.broadcastTitle.setText(n.getName());
        mViewHolder.broadcasterName.setText(n.getBroadcast());
        mViewHolder.contentText.setText(n.getSubject());
        //mViewHolder.broadcastThumbImage.setImageResource(R.drawable.dp_default_broadcast);


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
                Toast.makeText(getContext(),"uouououo",Toast.LENGTH_LONG).show();
            }
        });

        mViewHolder.attachmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog attachmentDialog = new Dialog(getContext());
                attachmentDialog.setTitle("Attachments");
                attachmentDialog.setContentView(R.layout.dialog_attachment);

                View rootView = inflater.inflate(R.layout.dialog_attachment,null);

                ListAdapter listAdapter = new CustomDownloadDialogAdapter(getContext(),NOTIFICATIONS);
                ListView downloadListView = (ListView)rootView.findViewById(R.id.downloadListView);
                downloadListView.setAdapter(listAdapter);

                attachmentDialog.show();
                attachmentDialog.setContentView(rootView);

                Button closeB = (Button)attachmentDialog.findViewById(R.id.closeDialog);
                closeB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
