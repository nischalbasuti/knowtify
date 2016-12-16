package com.hydratech19gmail.notify;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static String TAG = "QueryActivity";

    String mNotificationName;
    String mNotificationKey;
    String mUserKey;
    String mBroadcastKey;
    String mNotificationContent;

    DatabaseReference notificationRef;

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
        header.findViewById(R.id.dropDownMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"answer", "delete"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QueryActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(QueryActivity.this,"answer",Toast.LENGTH_SHORT).show();

                        } else if (which == 1) {
                            Toast.makeText(QueryActivity.this, "delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });

        //attaching header to listView
        listView.addHeaderView(header,null,false);

        //setting adapter
        listView.setAdapter(queryAdapter);

        listView.setOnItemClickListener(this);

        findViewById(R.id.fab_new_query).setOnClickListener(this);


        notificationRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mUserKey)
                .child("broadcasts").child(mBroadcastKey)
                .child("notifications").child(mNotificationKey);

        displayQueries(queryAdapter);
    }
    private void displayQueries(final ListAdapter listAdapter) {

        notificationRef.child("queries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getBaseContext(), "onDataChange queries", Toast.LENGTH_SHORT).show();

                queryObjects.clear();
                for(DataSnapshot query : dataSnapshot.getChildren()){
                    try{
                        QueryObject queryObject = query.getValue(QueryObject.class);
                        queryObject.setBroadcastKey(mBroadcastKey);
                        queryObject.setBroadcastUserKey(mUserKey);
                        queryObject.setNotificationKey(mNotificationKey);
                        queryObject.setQueryKey(query.getKey());

                        queryObjects.addFirst(queryObject);
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
                ((QueryAdapter)listAdapter).notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_new_query:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                NewQueryDialog newQueryDialog = new NewQueryDialog(this,user,notificationRef);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,AnswerActivity.class);

        TextView querySubject = (TextView) view.findViewById(R.id.query_subject);
        TextView queryContent = (TextView) view.findViewById(R.id.content);

        TextView queryKey = (TextView) view.findViewById(R.id.queryKey);
        String mNotificationName;
        String mNotificationContent;

        intent.putExtra("query_key",queryKey.getText().toString());
        intent.putExtra("notification_key",mNotificationKey);
        intent.putExtra("user_key",mUserKey);
        intent.putExtra("broadcast_key",mBroadcastKey);

        intent.putExtra("query_subject",querySubject.getText().toString());
        intent.putExtra("query_content",queryContent.getText().toString());

        startActivity(intent);
    }
}
