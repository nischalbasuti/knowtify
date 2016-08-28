package com.hydratech19gmail.notify;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BroadcastActivity";

    FirebaseUser mUser;

    String mBroadcastName;
    String mBroadcastInfo;
    String mUserId;
    String mPrivacy;
    String mBroadcastKey;

    final LinkedList<Notification> notifications = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mBroadcastName = getIntent().getExtras().getString("broadcastName");
        mBroadcastInfo = getIntent().getExtras().getString("broadcastInfo");
        mUserId = getIntent().getExtras().getString("userId");
        mPrivacy = getIntent().getExtras().getString("privacy");

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(mBroadcastName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

        final ListAdapter listAdapter = new CustomAdapter(this,notifications);
        final ListView listView = (ListView) findViewById(R.id.notificationList);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_broadcast,listView,false);

        //setting broadcast information
        ((TextView) header.findViewById(R.id.broadcast_info)).setText(mBroadcastInfo);
        ((TextView) header.findViewById(R.id.privacy)).setText(mPrivacy);
        ((TextView) header.findViewById(R.id.user_id)).setText(mUserId);

        ((TextView) header.findViewById(R.id.user_id)).setOnClickListener(this);

        //setting up drop down list
        ArrayList<String> dropDownList = new ArrayList<>();
        dropDownList.add("Settings");
        dropDownList.add("Delete");
        final PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(this,dropDownList);
        ((ImageView) header.findViewById(R.id.dropDownMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(view);
            }
        });


        listView.addHeaderView(header,null,false);
        listView.setHeaderDividersEnabled(true);
        listView.setAdapter(listAdapter);

        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/notifications");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                for(DataSnapshot notification : dataSnapshot.getChildren()){
                    try{
                        notifications.addFirst(notification.getValue(Notification.class));
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
                ((CustomAdapter)listAdapter).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FloatingActionButton newNotificationFab = (FloatingActionButton) findViewById(R.id.fab_new_notification);
        newNotificationFab.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_id:
                Intent intent = new Intent(this,BroadcasterProfileActivity.class);
                intent.putExtra("userId",mUserId);
                startActivity(intent);
                finish();
                break;
            case R.id.fab_new_notification:
                NewNotificationDialog newNotificationDialog = new NewNotificationDialog(this,mUser);
                newNotificationDialog.show();

                notifications.clear();
        }
    }
}
