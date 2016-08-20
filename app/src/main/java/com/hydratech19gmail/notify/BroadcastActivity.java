package com.hydratech19gmail.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class BroadcastActivity extends AppCompatActivity {

    private static final String TAG = "BroadcastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        String broadcastName = getIntent().getExtras().getString("broadcastName");
        String broadcastInfo = getIntent().getExtras().getString("broadcastInfo");

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(broadcastName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

        final List<Notification> notifications = new LinkedList<>();

        final ListAdapter listAdapter = new CustomAdapter(this,notifications);
        final ListView listView = (ListView) findViewById(R.id.notificationList);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_broadcast,listView,false);

        ((TextView) header.findViewById(R.id.broadcast_info)).setText(broadcastInfo);

        listView.addHeaderView(header,null,false);
        listView.setAdapter(listAdapter);

        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                for(DataSnapshot notification : dataSnapshot.getChildren()){
                    try{
                        notifications.add(notification.getValue(Notification.class));
                        ((CustomAdapter)listAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
