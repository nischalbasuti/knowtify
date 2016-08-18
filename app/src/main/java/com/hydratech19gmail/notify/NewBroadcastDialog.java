package com.hydratech19gmail.notify;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
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
    }

    @Override
    public void onClick(View view) {

    }
}
