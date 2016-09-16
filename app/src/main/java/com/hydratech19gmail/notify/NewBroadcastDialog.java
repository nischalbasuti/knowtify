package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

/*
 * Created by nischal on 18/8/16.
 */
public class NewBroadcastDialog extends Dialog implements View.OnClickListener {


    final FirebaseUser mUser;

    public NewBroadcastDialog(Context context, FirebaseUser user) {
        super(context);
        this.mUser = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_broadcast);

        Button newBroadcastButton = (Button) findViewById(R.id.new_broadcast_button);
        newBroadcastButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.new_broadcast_exit_button);
        exitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_broadcast_button:
                createNewBroadcast();
                break;
            case R.id.new_broadcast_exit_button:
                dismiss();
                break;
        }
    }

    private void createNewBroadcast() {
        EditText etBroadcastName = (EditText) findViewById(R.id.broadcast_name);
        EditText etBroadcastInfo = (EditText) findViewById(R.id.broadcast_info);

        String broadcastName = etBroadcastName.getText().toString();
        String broadcastInfo = etBroadcastInfo.getText().toString();

        if(broadcastName.equals("")) {
            Toast.makeText(getContext(),"Enter broadcast name",Toast.LENGTH_SHORT).show();
        }
        else {
            //..............

            //getting key value from aldsfjalsdf
            SharedPreferences sharedPref = getContext().getSharedPreferences("myprefs",Context.MODE_PRIVATE);
            String key = sharedPref.getString("user_key","user_key_doesnt_exist");

            Log.d("broadDialog","prefs key: "+key);

            //sending message to database
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference broadcastRef = ref.child("users/"+key+"/broadcasts");
            Broadcast newBroadcast = new Broadcast();

            newBroadcast.setName(broadcastName);
            newBroadcast.setInfo(broadcastInfo);
            //setting timeStamp
            Long tsLong = System.currentTimeMillis()/1000;

            String timeStamp = tsLong.toString();
            newBroadcast.setTimeStamp(timeStamp);

            RadioGroup privacyGroup = (RadioGroup) findViewById(R.id.radio_group_privacy);
            int selectedId = privacyGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.private_radio) {
                newBroadcast.setPrivacy("private");
            }
            else if(selectedId == R.id.public_radio) {
                newBroadcast.setPrivacy("public");

            }


            //pushing data
            broadcastRef.push().setValue(newBroadcast, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getContext(),"Created broadcast",Toast.LENGTH_SHORT).show();
                }
            });
            //................
        }

        dismiss();
    }
}
