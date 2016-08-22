package com.hydratech19gmail.notify;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by nischal on 18/8/16.
 */
public class BroadcastAdapter extends ArrayAdapter<Broadcast>{

    static final String TAG = "BroadcastAdapter";

    LayoutInflater mLayoutInflater;

    ViewHolder mViewHolder;
    ArrayList<String> mDropDownList;

    static class ViewHolder {
        public TextView broadcastName;
        public TextView broadcastInfo;
        public TextView userId;
        public TextView privacy;
        public ImageView dropDown;
    }

    public BroadcastAdapter(Context context, List<Broadcast> resource) {
        super(context, R.layout.broadcast_fragment_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.broadcast_fragment_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.broadcastName = (TextView) convertView.findViewById(R.id.broadcast_name);
            mViewHolder.broadcastInfo = (TextView) convertView.findViewById(R.id.broadcast_info);
            mViewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
            mViewHolder.privacy = (TextView) convertView.findViewById(R.id.privacy);
            mViewHolder.dropDown = (ImageView) convertView.findViewById(R.id.dropDownMenu);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView = null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView != null");
        }

        Broadcast broadcast = getItem(position);
        mViewHolder.broadcastName.setText(broadcast.getName());
        mViewHolder.broadcastInfo.setText(broadcast.getInfo());
        mViewHolder.userId.setText(broadcast.getUserId());
        mViewHolder.privacy.setText(broadcast.getPrivacy());

        //dropdown list stuff
        mDropDownList = new ArrayList<>();
        mDropDownList.add("Settings");
        mDropDownList.add("Delete");

        final PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(getContext(),mDropDownList);

        mViewHolder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(v,-5,0);
            }
        });

        return convertView;
    }
}
