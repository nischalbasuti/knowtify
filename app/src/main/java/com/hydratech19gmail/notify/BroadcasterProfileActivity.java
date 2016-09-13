package com.hydratech19gmail.notify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;

public class BroadcasterProfileActivity extends AppCompatActivity {

    String mUserId;
    private static final String TAG = "BroadcasterProfile";

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaster_profile);

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setTitle("User Name's Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.d(TAG,"error changing action bar");
            e.printStackTrace();
        }

        mUserId = getIntent().getExtras().getString("userId");

        final LinkedList<Broadcast> broadcasts = new LinkedList<>();

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

       /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference broadcastRef = ref.child("broadcasts");

        broadcastRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(),"broadcast update",Toast.LENGTH_SHORT).show();

                //TODO find better fix
                broadcasts.clear();

                for (DataSnapshot broadcast : dataSnapshot.getChildren()) {
                    try {
                        Broadcast addBroadcast = broadcast.getValue(Broadcast.class);
                        broadcasts.addFirst(addBroadcast);
                        ((BroadcastAdapter) listAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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
                finish();
            }
        });

        //showing dropdown on long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> mDropDownList;
                mDropDownList = new ArrayList<>();
                mDropDownList.add("Settings");
                mDropDownList.add("Delete");

                final PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(getApplicationContext(),mDropDownList);
                PopupWindow popupWindow = popupWindowDropDownMenu.popupWindowDropDownMenu();
                popupWindow.showAsDropDown(
                        view,
                        view.getWidth()/2 - popupWindow.getWidth()/2,
                        -view.getHeight()/2 + popupWindow.getHeight()/2
                );

                return true;
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

