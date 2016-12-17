package com.hydratech19gmail.notify;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "AnswerActivity";

    String mNotificationName;
    String mNotificationKey;
    String mUserKey;
    String mBroadcastKey;
    String mNotificationContent;
    String mQueryKey;
    String mTime;

    String mQuerySubject;
    String mQueryContent;

    DatabaseReference queryRef;

    final LinkedList<Answer> answers = new LinkedList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

       // mNotificationName = getIntent().getExtras().getString("notification_name");
        mNotificationKey = getIntent().getExtras().getString("notification_key");
        mUserKey = getIntent().getExtras().getString("user_key");
        mQueryKey = getIntent().getExtras().getString("query_key");
        mBroadcastKey = getIntent().getExtras().getString("broadcast_key");
        //mNotificationContent = getIntent().getExtras().getString("notification_content");
        mQuerySubject = getIntent().getExtras().getString("query_subject");
        mQueryContent = getIntent().getExtras().getString("query_content");
        mTime = getIntent().getExtras().getString("time");

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
        final AnswerAdapter listAdapter = new AnswerAdapter(this,answers);
        final ListView listView = (ListView) findViewById(R.id.answer_list);

        // TODO: 8/12/16 change header
        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.header_answer_list,listView,false);

        //adding header information
        ((TextView) header.findViewById(R.id.query_subject)).setText(mQuerySubject);
        ((TextView) header.findViewById(R.id.query_content)).setText(mQuerySubject);

        //userkey to display name
        final String[] username = new String[1];
        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mUserKey).child("username");
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((TextView) header.findViewById(R.id.userKey)).setText(username[0]);

        //setting drop down menu
        header.findViewById(R.id.dropDownMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"foo", "bar"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnswerActivity.this);
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(AnswerActivity.this,"foo",Toast.LENGTH_SHORT).show();

                        } else if (which == 1) {
                            Toast.makeText(AnswerActivity.this, "bar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });

        //attaching header to listView
        listView.addHeaderView(header,null,false);

        //setting adapter
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);

        findViewById(R.id.fab_new_answer).setOnClickListener(this);

        queryRef = FirebaseDatabase.getInstance().getReference().child("users/"+mUserKey
                +"/broadcasts/"+mBroadcastKey
                +"/notifications/"+mNotificationKey
                +"/queries/"+mQueryKey);

        displayAnswers(listAdapter);
        
    }

    private void displayAnswers(final ListAdapter listAdapter) {

        queryRef.child("answers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getBaseContext(), "onDataChange answers", Toast.LENGTH_SHORT).show();

                answers.clear();
                for(DataSnapshot ans : dataSnapshot.getChildren()){
                    try{
                        final Answer answer = ans.getValue(Answer.class);
                        answer.setBroadcastKey(mBroadcastKey);
                        answer.setBroadcastUserKey(mUserKey);
                        answer.setNotificationKey(mNotificationKey);
                        answer.setAnswerKey(ans.getKey());

                        //converting user key to display name and notifying adapter of data change
                        final String[] username = new String[1];
                        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(answer.getUserKey()).child("username");
                        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                username[0] = dataSnapshot.getValue().toString();
                                answer.setUserKey(username[0]);
                                answers.addFirst(answer);

                                ((AnswerAdapter)listAdapter).notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
                ((AnswerAdapter)listAdapter).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_new_answer:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                
                NewAnswerDialog newAnswerDialog = new NewAnswerDialog(this,user,queryRef);
                newAnswerDialog.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
