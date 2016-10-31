package com.hydratech19gmail.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = "QueryActivity";

    String mNotificationName;
    String mNotificationKey;
    String mUserKey;
    String mBroadcastKey;
    String mNotificationContent;

    final LinkedList<QueryObject> queryObjects = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        //initializing data from intent
        mNotificationName = getIntent().getExtras().getString("notification_name");
        mNotificationKey = getIntent().getExtras().getString("notification_key");
        mUserKey = getIntent().getExtras().getString("user_key");
        mBroadcastKey = getIntent().getExtras().getString("broadcast_key");
        mNotificationContent = getIntent().getExtras().getString("notification_content");


        //changing actionbar title
        try {
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(mNotificationName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

        //initialize listView adapter
        final ListAdapter queryAdapter = new QueryAdapter(this,queryObjects);
        final ListView listView = (ListView) findViewById(R.id.query_list);

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.header_query_list,listView,false);

        //adding header information
        ((TextView) header.findViewById(R.id.notification_title)).setText(mNotificationName);
        ((TextView) header.findViewById(R.id.notification_content)).setText(mNotificationContent);

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

        //attaching header to listView
        listView.addHeaderView(header,null,false);

        //setting adapter
        listView.setAdapter(queryAdapter);

        findViewById(R.id.fab_new_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_new_query:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                NewQueryDialog newQueryDialog = new NewQueryDialog(this,user);
                newQueryDialog.show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
