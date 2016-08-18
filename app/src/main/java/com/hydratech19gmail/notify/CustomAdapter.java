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
    public CustomAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mDropDownList = new ArrayList<>();
        mDropDownList.add("Delete");
        mDropDownList.add("Mark as read");

        mPopupWindowDropDownMenu = PopupWindowDropDownMenu();


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View vi = inflater.inflate(R.layout.home_fragment_row,parent,false);

        Notification n = getItem(position);
        //String broadcaster = getItem(position);
        //String content = getItem(position);

        TextView broadcastTitle = (TextView)vi.findViewById(R.id.broadcastTitle);
        ImageView broadcastThumbImage = (ImageView)vi.findViewById(R.id.thumbnail_image);
        TextView broadcasterName = (TextView)vi.findViewById(R.id.broadcasterName);
        TextView contentText = (TextView)vi.findViewById(R.id.content);
        ImageView dropDownImage = (ImageView)vi.findViewById(R.id.dropDownMenu);

        dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mPopupWindowDropDownMenu.showAsDropDown(v,-5,0);
            }
        });

        broadcastTitle.setText(n.getData1());
        broadcasterName.setText(n.getData2());
        contentText.setText(n.getData3());

        broadcastThumbImage.setImageResource(R.drawable.dp_default_broadcast);

        Log.d("CustomAdapter","getView");

        return vi;
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
