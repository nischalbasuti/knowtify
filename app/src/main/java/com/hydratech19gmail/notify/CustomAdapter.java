package com.hydratech19gmail.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    }

    public CustomAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

            convertView.setTag(mViewHolder);

            Log.d("CustomAdapter","getView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

            Log.d("CustomAdapter","getView | not null");
        }


        Notification n = getItem(position);

        mViewHolder.broadcastTitle.setText(n.getName());
        mViewHolder.broadcasterName.setText(n.getBroadcast());
        mViewHolder.contentText.setText(n.getSubject());
        //mViewHolder.broadcastThumbImage.setImageResource(R.drawable.dp_default_broadcast);


        //setting dropdown
        mViewHolder.dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //new PopupWindowDropDownMenu(getContext(),mDropDownList).popupWindowDropDownMenu().showAsDropDown(view);
                final Dialog attachmentDialog = new Dialog(getContext());

                List<String> list = new ArrayList<String>();
                list.add("Delete");
                list.add("Mark as Read");
                ArrayAdapter<String> adapter;

                attachmentDialog.setContentView(R.layout.dialog_dropdown);

                View rootView = inflater.inflate(R.layout.dialog_dropdown,null);

                adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_expandable_list_item_1,list);

                ListView downloadListView = (ListView)rootView.findViewById(R.id.dropdownListView);
                downloadListView.setAdapter(adapter);

                attachmentDialog.show();
                attachmentDialog.setContentView(rootView);

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

        return convertView;
    }
}
