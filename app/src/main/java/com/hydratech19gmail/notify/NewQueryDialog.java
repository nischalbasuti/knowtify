package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by nischal on 31/10/16.
 */

class NewQueryDialog extends Dialog implements View.OnClickListener{
    private final FirebaseUser mUser;
    private final DatabaseReference notificationRef;

    private final static String TAG = "newQueryDialog";

    NewQueryDialog(Context context, FirebaseUser user, DatabaseReference notificationRef) {
        super(context);
        this.mUser = user;
        this.notificationRef = notificationRef;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_query);

        Button newqueryButton = (Button) findViewById(R.id.new_query_button);
        newqueryButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.new_query_exit_button);
        exitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_query_button:
                createNewQuery();
                break;
            case R.id.new_query_exit_button:
                dismiss();
                break;
        }
    }

    private void createNewQuery() {
        EditText etquerySubject = (EditText) findViewById(R.id.query_subject);
        EditText etqueryContent = (EditText) findViewById(R.id.query_content);

        String querySubject = etquerySubject.getText().toString();
        String queryContent = etqueryContent.getText().toString();

        if(querySubject.equals("")) {
            Toast.makeText(getContext(),"Enter query name",Toast.LENGTH_SHORT).show();
        }
        else if(queryContent.isEmpty()) {
            Toast.makeText(getContext(),"Enter query info",Toast.LENGTH_SHORT).show();
        }
        else {
            //..............

            //sending message to database
            final DatabaseReference queryRef = notificationRef.child("queries");

            //calculating timestamp
            Long tsLong = System.currentTimeMillis()/1000;
            String timeStamp = tsLong.toString();

            //creating new query
            QueryObject newQuery = new QueryObject();

            newQuery.setQueryContent(queryContent);
            newQuery.setQuerySubject(querySubject);
            newQuery.setQueryUserKey(mUser.getUid());
            newQuery.setTimeStamp(timeStamp);
            newQuery.setRating(1);

            //pushing data
            queryRef.push().setValue(newQuery, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d(TAG,"query path: "+queryRef.toString());
                    Toast.makeText(getContext(),"Created query",Toast.LENGTH_SHORT).show();
                }
            });
            //................
        }

        dismiss();
    }
}
