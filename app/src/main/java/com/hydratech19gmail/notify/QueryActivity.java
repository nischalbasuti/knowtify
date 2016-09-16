package com.hydratech19gmail.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

public class QueryActivity extends AppCompatActivity {

    final LinkedList<QueryObject> queryObjects = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        //initialize listView adapter
        final ListAdapter queryAdapter = new QueryAdapter(this,queryObjects);
        final ListView listView = (ListView) findViewById(R.id.query_list);

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.header_query_list,listView,false);

        //setting drop down menu
        final ArrayList<String> headerDropdownList = new ArrayList<>();
        headerDropdownList.add("asdf");
        headerDropdownList.add("settings");
        ((ImageView)header.findViewById(R.id.dropDownMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(getApplicationContext(),headerDropdownList);
                popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(view);
            }
        });

        //TODO add header info

        //attaching header to listView
        listView.addHeaderView(header,null,false);

        //setting adapter
        listView.setAdapter(queryAdapter);

    }
}
