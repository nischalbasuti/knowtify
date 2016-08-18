package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Created by nischal on 18/8/16.
 */
public class NewBroadcastDialog extends Dialog implements View.OnClickListener {
    public NewBroadcastDialog(Context context) {
        super(context);
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
        TextView tvBroadcastName = (TextView) findViewById(R.id.broadcast_name);
        String broadcastName = tvBroadcastName.getText().toString();

        if(broadcastName.equals("")) {
            Toast.makeText(getContext(),"Enter broadcast name",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(),"Created broadcast",Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}
