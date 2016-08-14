package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class HomeFragment extends Fragment{

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final List<Notification> notifications = new LinkedList<>();

        View rootView = inflater.inflate(R.layout.home_fragment,container,false);

        final ListAdapter listAdapter = new CustomAdapter(this.getContext(),notifications);
        final ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);

        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "onDataChange", Toast.LENGTH_SHORT).show();
                Notification n = new Notification();

                Notification ntf = new Notification();
                for(DataSnapshot notification : dataSnapshot.getChildren()){

                    notifications.add(notification.getValue(Notification.class));
                    ((CustomAdapter)listAdapter).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;

    }
}
