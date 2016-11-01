package com.hydratech19gmail.notify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by nischal on 15/9/16.
 */
public class QueryAdapter extends ArrayAdapter<QueryObject> {
    static final String TAG = "QueryAdapter";

    LayoutInflater mLayoutInflater;

    ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView querySubject;
        public TextView queryContent;
        public TextView queryUserKey;
        public ImageView dropDown;
    }

    ArrayList<String> dropDownList = new ArrayList<>();

    public QueryAdapter(Context context, List<QueryObject> resource) {
        super(context, R.layout.query_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

        dropDownList.add("hide");
        dropDownList.add("remove");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.query_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.querySubject = (TextView) convertView.findViewById(R.id.query_subject);
            mViewHolder.queryContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.queryUserKey = (TextView) convertView.findViewById(R.id.query_userid);
            mViewHolder.dropDown = (ImageView) convertView.findViewById(R.id.dropDownMenu);
            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        QueryObject query = getItem(position);
        mViewHolder.querySubject.setText(query.getQuerySubject());
        mViewHolder.queryContent.setText(query.getQueryContent());
        mViewHolder.queryUserKey.setText(query.getQueryUserKey());

        mViewHolder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(getContext(),dropDownList);
                popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(view);
            }
        });
        return convertView;
    }
}
