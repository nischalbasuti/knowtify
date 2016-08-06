package com.hydratech19gmail.notify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jaelse on 01-08-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, String[] data){
        super(context,R.layout.home_fragment_row,data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View vi = inflater.inflate(R.layout.home_fragment_row,parent,false);

        String title = getItem(position);
        String broadcaster = getItem(position);
        String content = getItem(position);

        TextView broadcastTitle = (TextView)vi.findViewById(R.id.broadcastTitle);
        ImageView broadcastThumbImage = (ImageView)vi.findViewById(R.id.thumbnail_image);
        TextView broadcasterName = (TextView)vi.findViewById(R.id.broadcasterName);
        TextView contentText = (TextView)vi.findViewById(R.id.content);

        broadcastTitle.setText(title);

        broadcastThumbImage.setImageResource(R.drawable.dp_default_broadcast);

        Log.d("text","getView");
        return vi;
    }
}
