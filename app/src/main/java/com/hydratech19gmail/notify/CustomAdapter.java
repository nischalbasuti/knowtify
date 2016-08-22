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
import java.util.List;

/**
 * Created by Jaelse on 01-08-2016.
 */
public class CustomAdapter extends ArrayAdapter<Notification> {
    ArrayList<String> mDropDownList;
    PopupWindow mPopupWindowDropDownMenu;

    LayoutInflater inflater;

    public CustomAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
        mViewHolder = new ViewHolder();
        inflater = LayoutInflater.from(getContext());
    }
    
    ViewHolder mViewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mDropDownList = new ArrayList<>();
        mDropDownList.add("Delete");
        mDropDownList.add("Mark as read");

        mPopupWindowDropDownMenu = PopupWindowDropDownMenu();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.home_fragment_row,parent,false);

            mViewHolder.broadcastTitle = (TextView)convertView.findViewById(R.id.broadcastTitle);
            mViewHolder.broadcastThumbImage = (ImageView)convertView.findViewById(R.id.thumbnail_image);
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



        mViewHolder.dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mPopupWindowDropDownMenu.showAsDropDown(v,-5,0);
            }
        });

        mViewHolder.broadcastTitle.setText(n.getData1());
        mViewHolder.broadcasterName.setText(n.getData2());
        mViewHolder.contentText.setText(n.getData3());
        mViewHolder.broadcastThumbImage.setImageResource(R.drawable.dp_default_broadcast);

        return convertView;
    }
    
    static class ViewHolder {
        TextView broadcastTitle;
        ImageView broadcastThumbImage;
        TextView broadcasterName;
        TextView contentText;
        ImageView dropDownImage;
    }

    public PopupWindow PopupWindowDropDownMenu(){

        PopupWindow popupWindow = new PopupWindow(this.getContext());

        ListView listView = new ListView(this.getContext());

        listView.setAdapter(dropDownAdapter(mDropDownList));

        listView.setOnItemClickListener(new DropDownMenuOnItemClickListener());

        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setContentView(listView);

        return popupWindow;
    }

    public ArrayAdapter<String> dropDownAdapter(ArrayList<String> mDropDownList){

        final String[] list = new String[mDropDownList.size()];
        mDropDownList.toArray(list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,list){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String item = getItem(position);


                TextView listText = new TextView(this.getContext());

                listText.setText(item);
                listText.setTextSize(22);
                listText.setPadding(10, 10, 10, 10);
                listText.setTextColor(Color.WHITE);

                return listText;
            }
        };

        return arrayAdapter;
    }
}
