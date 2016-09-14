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
 * Created by nischal on 15/9/16.
 */
public class QueryAdapter extends ArrayAdapter<QueryObject>{
        static final String TAG = "QueryAdapter";

        LayoutInflater mLayoutInflater;

        ViewHolder mViewHolder;

static class ViewHolder {
    public TextView queryName;
    public TextView queryContent;
    public TextView queryUserId;
}

    public QueryAdapter(Context context, List<QueryObject> resource) {
        super(context, R.layout.query_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.query_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.queryName = (TextView) convertView.findViewById(R.id.query_title);
            mViewHolder.queryContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.queryUserId = (TextView) convertView.findViewById(R.id.query_userid);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        QueryObject query = getItem(position);
        mViewHolder.queryName.setText(query.getQueryName());
        mViewHolder.queryContent.setText(query.getQueryContent());
        mViewHolder.queryUserId.setText(query.getQueryUserId());

        return convertView;
    }
}
