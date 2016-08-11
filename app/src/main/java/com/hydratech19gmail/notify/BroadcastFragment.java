package com.hydratech19gmail.notify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.firebase.client.Firebase;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class BroadcastFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.broadcast_fragment,container,false);

        final EditText data1 = (EditText)rootView.findViewById(R.id.editText);
        final EditText data2 = (EditText)rootView.findViewById(R.id.editText2);

        Button sendB = (Button)rootView.findViewById(R.id.button);

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"file uploaded",Toast.LENGTH_LONG).show();
                Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");
                Notification notification = new Notification(data1.getText().toString(),data2.getText().toString());

                ref.push().setValue(notification);
                data1.setText("");
                data2.setText("");

            }
        });
        return rootView;
    }
}
