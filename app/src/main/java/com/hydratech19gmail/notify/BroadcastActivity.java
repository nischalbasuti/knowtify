package com.hydratech19gmail.notify;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

        try {
            getSupportActionBar().setTitle("Name of Broadcast");
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

        final List<Notification> notifications = new LinkedList<>();

        final ListAdapter listAdapter = new CustomAdapter(this,notifications);
        final ListView listView = (ListView) findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);


        final RelativeLayout headerContainer = (RelativeLayout) findViewById(R.id.header_container);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }
            int lastVisibleItem = 0;
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               /* if(firstVisibleItem > lastVisibleItem){
                    headerContainer.animate().translationY(-headerContainer.getHeight())
                            .setDuration(1)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    headerContainer.setVisibility(View.GONE);
                                }
                            });
                }
                else if(firstVisibleItem < lastVisibleItem ){
                    headerContainer.animate().translationY(0).setDuration(1);
                    headerContainer.setVisibility(View.VISIBLE);
                }
                lastVisibleItem  = firstVisibleItem;
                */
            }
        });

        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                for(DataSnapshot notification : dataSnapshot.getChildren()){
                    notifications.add(notification.getValue(Notification.class));
                    ((CustomAdapter)listAdapter).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
