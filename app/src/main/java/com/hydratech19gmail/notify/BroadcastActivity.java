package com.hydratech19gmail.notify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BroadcastActivity";

    FirebaseUser mUser;

    String mBroadcastKey;
    String mBroadcastName;
    String mBroadcastInfo;
    String mUserId;
    String mPrivacy;

    final LinkedList<Notification> notifications = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        //getting user info
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting data from previous activity
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

        //initializing listView and it's adapter
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
        dropDownList.add("Subscribe");
        final PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(this,dropDownList);
        ((ImageView) header.findViewById(R.id.dropDownMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popupWindowDropDownMenu.popupWindowDropDownMenu().showAsDropDown(view);

                String[] options = {"Subscribe", "Delete"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BroadcastActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(BroadcastActivity.this, "Subscribe", Toast.LENGTH_SHORT).show();

                            String path = "users/"+mUserId+"/broadcasts/"+mBroadcastKey+"/subscribers/";
                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);

                            SharedPreferences sharedPreferences = BroadcastActivity.this.getSharedPreferences("myprefs", MODE_PRIVATE);
                            String prefUserKey = sharedPreferences.getString("user_key", "user key doesnt exits");
                            String prefToken = sharedPreferences.getString("device_token", "device token doesn't exit");
                            Subscriber subscriber  = new Subscriber(prefUserKey,prefToken);

                            ref.push().setValue(subscriber);

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

        displayNotifications(listAdapter);

        //setting on click listener fo new notification fab
        FloatingActionButton newNotificationFab = (FloatingActionButton) findViewById(R.id.fab_new_notification);
        newNotificationFab.setOnClickListener(this);

    }

    private void displayNotifications(final ListAdapter listAdapter) {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        final String prefUserKey = sharedPreferences.getString("user_key","user key doesnt exits");

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //finding broadcast key
        ref.child("users").child(prefUserKey).child("broadcasts").orderByChild("name").equalTo(mBroadcastName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if broadcast key is found, display notifications
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            mBroadcastKey = childSnapshot.getKey();
                            Log.d("NewNotif","broadcast key: "+mBroadcastKey);

                            DatabaseReference notificationRef = ref.child("users/"+prefUserKey+"/broadcasts/"+mBroadcastKey+"/notifications/");
                            notificationRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Toast.makeText(getBaseContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                                    //TODO find a better fix

                                    notifications.clear();
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
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_id://starting activity to display broadcasters profile
                Intent intent = new Intent(this,BroadcasterProfileActivity.class);
                intent.putExtra("userId",mUserId);
                startActivity(intent);
                finish();
                break;
            case R.id.fab_new_notification://starting dialog box to create new notification
            //    NewNotificationDialog newNotificationDialog = new NewNotificationDialog(this,mUser);
            //    newNotificationDialog.show();
                Intent intent1 = new Intent(this,NewNotificationDialog.class);
                intent1.putExtra("broadcastName",mBroadcastName);
                startActivity(intent1);
        }
    }

    String path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("something", DatabaseUtils.dumpCursorToString(cursor));
            int columnIndex = cursor.getColumnIndex(projection[0]);

            path = cursor.getString(columnIndex);

            Toast.makeText(this,path,Toast.LENGTH_LONG).show();
            cursor.close();

            //uploading file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://notify-1384.appspot.com");
            try{

                Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("NotifDialog: ","file thing: "+exception.getMessage());
                        Toast.makeText(getBaseContext(),"error uploading file",Toast.LENGTH_LONG).show();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
            } catch (Exception e) {
                Log.d("NotifDialog: ","file thing: "+e.getMessage());
                //e.printStackTrace();
            }
        }
    }
}
