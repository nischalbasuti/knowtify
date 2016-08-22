package com.hydratech19gmail.notify;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.LinkedList;
import java.util.List;

public class BroadcasterProfileActivity extends AppCompatActivity {

    String mUserId;
    private static final String TAG = "BroadcasterProfile";

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaster_profile);
        
        mUserId = getIntent().getExtras().getString("userId");

        final List<Broadcast> broadcasts = new LinkedList<>();

        final ListAdapter listAdapter = new BroadcastAdapter(getApplicationContext(),broadcasts);
        final ListView listView = (ListView) findViewById(R.id.broadcast_list);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_broadcaster_profile,listView,false);
        listView.addHeaderView(header,null,false);
        listView.setAdapter(listAdapter);

        try{
            ((TextView)header.findViewById(R.id.user_id)).setText(mUserId);
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"error setting user id");
        }

        Firebase firebase = new Firebase("https://notify-1384.firebaseio.com/");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"broadcast update",Toast.LENGTH_SHORT).show();

                for (DataSnapshot broadcast : dataSnapshot.getChildren()) {
                    try {
                        Broadcast addBroadcast = broadcast.getValue(Broadcast.class);
                        if(addBroadcast.getPrivacy() != null) {
                            broadcasts.add(addBroadcast);
                            ((BroadcastAdapter) listAdapter).notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //on  item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),BroadcastActivity.class);

                TextView broadcastName = (TextView) view.findViewById(R.id.broadcast_name);
                TextView broadcastInfo = (TextView) view.findViewById(R.id.broadcast_info);
                TextView userId = (TextView) view.findViewById(R.id.user_id);
                TextView privacy = (TextView) view.findViewById(R.id.privacy);

                intent.putExtra("broadcastName", broadcastName.getText().toString());
                intent.putExtra("broadcastInfo", broadcastInfo.getText().toString());
                intent.putExtra("userId",userId.getText().toString());
                intent.putExtra("privacy",privacy.getText().toString());

                startActivity(intent);
            }
        });
    }
}

