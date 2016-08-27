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

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            //sending message to database
            Firebase ref = new Firebase("https://notify-1384.firebaseio.com/broadcasts/");
            Broadcast newBroadcast = new Broadcast();

            newBroadcast.setName(broadcastName);
            newBroadcast.setInfo(broadcastInfo);
            try{
                newBroadcast.setUserId(mUser.getEmail());
            } catch (Exception e) {
                newBroadcast.setUserId("faiiiilllll");
            }

            RadioGroup privacyGroup = (RadioGroup) findViewById(R.id.radio_group_privacy);
            int selectedId = privacyGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.private_radio) {
                newBroadcast.setPrivacy("private");
            }
            else if(selectedId == R.id.public_radio) {
                newBroadcast.setPrivacy("public");
            }

            ref.push().setValue(newBroadcast);
            //................

            Toast.makeText(getContext(),"Created broadcast",Toast.LENGTH_SHORT).show();
        }

        dismiss();
    }
}
