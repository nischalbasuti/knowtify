package com.hydratech19gmail.notify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/*
 * Created by nischal on 18/8/16.
 */
public class BroadcastAdapter extends ArrayAdapter<Broadcast>{

    static final String TAG = "BroadcastAdapter";

    LayoutInflater mLayoutInflater;

    ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView broadcastName;
        public TextView broadcastInfo;
        public TextView privacy;
        public TextView time;
        public TextView userKey;
    }

    public BroadcastAdapter(Context context, List<Broadcast> resource) {
        super(context, R.layout.broadcast_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.broadcast_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.broadcastName = (TextView) convertView.findViewById(R.id.broadcast_name);
            mViewHolder.broadcastInfo = (TextView) convertView.findViewById(R.id.broadcast_info);
            mViewHolder.privacy = (TextView) convertView.findViewById(R.id.privacy);
            mViewHolder.time = (TextView) convertView.findViewById(R.id.time);
            mViewHolder.userKey = (TextView) convertView.findViewById(R.id.userKey);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        Broadcast broadcast = getItem(position);
        mViewHolder.broadcastName.setText(broadcast.getName());
        mViewHolder.broadcastInfo.setText(broadcast.getInfo());
        mViewHolder.privacy.setText(broadcast.getPrivacy());
        mViewHolder.userKey.setText(broadcast.getUserKey());

        mViewHolder.time.setText(DateConvert.timeStampToDate(Long.parseLong(broadcast.getTimeStamp())*1000));

        return convertView;
    }


}
