package com.hydratech19gmail.notify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class BroadcastFragment extends Fragment {
    RequestQueue
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EditText data1 = (EditText)container.findViewById(R.id.editText);
        EditText data2 = (EditText)container.findViewById(R.id.editText2);

        Button sendB = (Button)container.findViewById(R.id.button);

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://notify-1384.appspot.com/";

            }
        });
        return inflater.inflate(R.layout.broadcast_fragment,container,false);

    }
}
