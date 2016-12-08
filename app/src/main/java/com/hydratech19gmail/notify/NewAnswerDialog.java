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
 * Created by nischal on 8/12/16.
 */

public class NewAnswerDialog extends Dialog implements View.OnClickListener{
    final FirebaseUser mUser;

    DatabaseReference queryRef;

    public NewAnswerDialog(Context context, FirebaseUser user, DatabaseReference queryRef) {
        super(context);
        this.mUser = user;
        this.queryRef = queryRef;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_answer);

        Button newAnswerButton = (Button) findViewById(R.id.new_answer_button);
        newAnswerButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.new_answer_exit_button);
        exitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_answer_button:
                submitAnswer();
                break;
            case R.id.new_answer_exit_button:
                dismiss();
                break;
        }
    }

    private void submitAnswer() {
        EditText etAnswerContent = (EditText) findViewById(R.id.answer_content);
        
        String answerContent = etAnswerContent.getText().toString();
        
        if(answerContent.equals("")) {
            Toast.makeText(getContext(),"Enter answer",Toast.LENGTH_SHORT).show();
        }
        else {
            //..............

            Answer newAnswer = new Answer();

            //setting timeStamp
            Long tsLong = System.currentTimeMillis()/1000;
            String timeStamp = tsLong.toString();

            //adding data to new answer object
            newAnswer.setTimeStamp(timeStamp);
            newAnswer.setUserKey(mUser.getUid());
            newAnswer.setContent(answerContent);

            //pushing data
            queryRef.child("answers").push().setValue(newAnswer, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getContext(),"submitted answer",Toast.LENGTH_SHORT).show();
                }
            });
            //................
        }

        dismiss();
    }
}
