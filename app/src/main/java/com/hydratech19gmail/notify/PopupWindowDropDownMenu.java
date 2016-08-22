package com.hydratech19gmail.notify;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Created by nischal on 21/8/16.
 */
public class PopupWindowDropDownMenu {
    Context context;
    ArrayList<String> mDropDownList;

    PopupWindowDropDownMenu(Context context, ArrayList<String> dropDownList){
        this.context = context;
        this.mDropDownList = dropDownList;
    }

    public PopupWindow popupWindowDropDownMenu(){

        PopupWindow popupWindow = new PopupWindow(context);

        ListView listView = new ListView(context);

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,list){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String item = getItem(position);


                TextView listText = new TextView(context);

                listText.setText(item);
                listText.setTextSize(15);
                listText.setPadding(5, 5, 5, 5);
                listText.setTextColor(Color.WHITE);

                return listText;
            }
        };

        return arrayAdapter;
    }
}
