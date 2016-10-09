package com.hydratech19gmail.notify;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


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
        mDropDownList = new ArrayList<>();
        mDropDownList.add("Delete");
        mDropDownList.add("Mark as read");

        mViewHolder.dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupWindowDropDownMenu(getContext(),mDropDownList).popupWindowDropDownMenu().showAsDropDown(view);
            }
        });

        return convertView;
    }
}
