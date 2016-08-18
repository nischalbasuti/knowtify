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

    LayoutInflater mLayoutInflater;
    ViewHolder mViewHolder;

    static final String TAG = "BroadcastAdapter";

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

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView = null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView != null");
        }

        Broadcast broadcast = getItem(position);
        mViewHolder.broadcastName.setText(broadcast.getData1());
        mViewHolder.broadcastInfo.setText(broadcast.getData2());

        return convertView;
    }

    static class ViewHolder {
        public TextView broadcastName;
        public TextView broadcastInfo;
    }
}
