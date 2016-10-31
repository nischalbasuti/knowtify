package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nischal on 31/10/16.
 */

public class NewQueryDialog extends Dialog implements View.OnClickListener{
    final FirebaseUser mUser;

    public NewQueryDialog(Context context, FirebaseUser user) {
        super(context);
        this.mUser = user;
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
        EditText etqueryName = (EditText) findViewById(R.id.query_subject);
        EditText etqueryInfo = (EditText) findViewById(R.id.query_content);

        String queryName = etqueryName.getText().toString();
        String queryInfo = etqueryInfo.getText().toString();

        if(queryName.equals("")) {
            Toast.makeText(getContext(),"Enter query name",Toast.LENGTH_SHORT).show();
        }
        else {
            //..............

            //sending message to database
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            //TODO change query ref
            DatabaseReference queryRef = ref.child("users/"+mUser.getUid()+"/querys");
            QueryObject newQuery = new QueryObject();

            //pushing data
            queryRef.push().setValue(newQuery, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getContext(),"Created query",Toast.LENGTH_SHORT).show();
                }
            });
            //................
        }

        dismiss();
    }
}
