package com.hydratech19gmail.notify;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BroadcastActivity";

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    String prefToken;

    FirebaseUser mUser;

    String mBroadcastKey;
    String mBroadcastName;
    String mBroadcastInfo;
    String mBroadcasterUID;
    String mPrivacy;
    Boolean subscribed = false;

    final LinkedList<Notification> notifications = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        prefToken = sharedPreferences.getString("device_token", "device token doesn't exit");

        //getting user info
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting data from previous activity
        mBroadcastName = getIntent().getExtras().getString("broadcastName");
        mBroadcastInfo = getIntent().getExtras().getString("broadcastInfo");
        mBroadcasterUID = getIntent().getExtras().getString("userId");
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

        //initializing listView and it's adapter
        final ListAdapter listAdapter = new CustomAdapter(this,notifications);
        final ListView listView = (ListView) findViewById(R.id.notificationList);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_broadcast,listView,false);

        //setting broadcast information
        ((TextView) header.findViewById(R.id.broadcast_info)).setText(mBroadcastInfo);
        ((TextView) header.findViewById(R.id.privacy)).setText(mPrivacy);

        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mBroadcasterUID).child("username");
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue().toString();
                ((TextView) header.findViewById(R.id.user_id)).setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // ((TextView) header.findViewById(R.id.user_id)).setText(mBroadcasterUID);


        header.findViewById(R.id.user_id).setOnClickListener(this);

        header.findViewById(R.id.thumbnail).setOnClickListener(this);

        //setting up drop down list
        header.findViewById(R.id.dropDownMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(view);
                String[] options;
                if(subscribed){
                    options = new String[]{"Unsubscribe", "Delete"};
                } else {
                    options = new String[]{"Subscribe", "Delete"};
                }
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BroadcastActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            try {
                                new AsyncTask<Void,Void,Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void[] voids) {
                                        Log.d(TAG,"async task sub");
                                        if(subscribed){
                                            unsubscribeBroadcast();
                                            return 0;
                                        } else {
                                            subscribeBroadcast();
                                            return 1;
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute(Integer taskid) {
                                        if(taskid == 0){
                                            //update layout
                                            Toast.makeText(BroadcastActivity.this,"async task unsubscribe",Toast.LENGTH_SHORT).show();
                                        } else if (taskid == 1){
                                            Toast.makeText(BroadcastActivity.this,"async task subscribe",Toast.LENGTH_SHORT).show();
                                            //show error
                                        }
                                    }

                                }.execute().get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else if (which == 1) {
                            Toast.makeText(BroadcastActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });


        //setting header containing broadcast information to listView
        listView.addHeaderView(header,null,false);

        //setting adapter
        listView.setAdapter(listAdapter);

        displayNotifications(listAdapter); // also retrieves broadcastKey

        //setting on click listener fo new notification fab
        FloatingActionButton newNotificationFab = (FloatingActionButton) findViewById(R.id.fab_new_notification);
        newNotificationFab.setOnClickListener(this);

    }

    private void subscribeBroadcast() {

        //adding userId to subscribers
        String path = "users/"+mBroadcasterUID+"/broadcasts/"+mBroadcastKey+"/subscribers/";
        Log.d(TAG,"sub path: "+path);
        DatabaseReference subsRef = ref.child("users")
                .child(mBroadcasterUID)
                .child("broadcasts")
                .child(mBroadcastKey)
                .child("subscribers")
                .child(mUser.getUid());
        Subscriber subscriber = new Subscriber(mUser.getUid(),prefToken);
        subsRef.setValue(subscriber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                subscribed = true;
            }
        });

        Subscription subscription = new Subscription(mBroadcasterUID,mBroadcastKey,mUser.getUid());
        ref.child("users").child(mUser.getUid()).child("subscriptions").child(mBroadcastKey).setValue(subscription);
    }

    private void unsubscribeBroadcast(){
        //removing userId from subscribers
        DatabaseReference subsRef = ref.child("users")
                .child(mBroadcasterUID)
                .child("broadcasts")
                .child(mBroadcastKey)
                .child("subscribers")
                .child(mUser.getUid());
        subsRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                subscribed = false;
            }
        });

        ref.child("users").child(mUser.getUid()).child("subscriptions").child(mBroadcastKey).setValue(null);
    }

    private void displayNotifications(final ListAdapter listAdapter) {
        //finding broadcast key
        ref.child("users")
                .child(mBroadcasterUID)
                .child("broadcasts")
                .orderByChild("name")
                .equalTo(mBroadcastName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if broadcast key is found, display notifications
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            mBroadcastKey = childSnapshot.getKey();
                            Log.d(TAG,"broadcast key: "+mBroadcastKey);

                            //**********************checking if subscribed**************************
                            DatabaseReference subsRef = ref.child("users")
                                    .child(mBroadcasterUID)
                                    .child("broadcasts")
                                    .child(mBroadcastKey)
                                    .child("subscribers");
                            subsRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                        String key = childSnapshot.getKey();
                                        Log.d(TAG,"user key: "+key);

                                        subscribed = true;
                                        //old subscriber new/old device
                                       // ref.setValue(prefToken);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //**********************************************************************

                            DatabaseReference notificationRef = ref.child("users/"
                                                                            +mBroadcasterUID
                                                                            +"/broadcasts/"
                                                                            +mBroadcastKey
                                                                            +"/notifications/");
                            notificationRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Toast.makeText(getBaseContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                                    notifications.clear();
                                    for(DataSnapshot notification : dataSnapshot.getChildren()){
                                        try{
                                            Notification notification1 = notification.getValue(Notification.class);
                                            notification1.setNotificationKey(notification.getKey());
                                            notifications.addFirst(notification1);
                                        } catch (Exception e) {
                                            Log.d(TAG,e.getMessage());
                                        }
                                    }
                                    ((CustomAdapter)listAdapter).notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_id://starting activity to display broadcasters profile
                Intent intent = new Intent(this,BroadcasterProfileActivity.class);
                intent.putExtra("userId",mBroadcasterUID);
                startActivity(intent);
                finish();
                break;
            case R.id.fab_new_notification://starting dialog box to create new notification
                Intent intent1 = new Intent(this,NewNotificationDialog.class);
                intent1.putExtra("broadcastName",mBroadcastName);
                startActivity(intent1);
                break;
            case R.id.thumbnail:
                subscribeBroadcast();
                break;
        }
    }
}
