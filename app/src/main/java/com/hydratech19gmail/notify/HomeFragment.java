package com.hydratech19gmail.notify;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by Jaelse on 30-07-2016.
 */
public class HomeFragment extends Fragment{

    private static final String TAG = "HomeFrgmnt";

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.home_fragment,container,false);


        final LinkedList<Notification> notifications = new LinkedList<>();
        final ListAdapter listAdapter = new CustomAdapter(this.getContext(),notifications);
        final ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);

        Firebase ref = new Firebase("https://notify-1384.firebaseio.com/notifications/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                //TODO find a better fix
                notifications.clear();

                for(DataSnapshot notification : dataSnapshot.getChildren()){
                    try{
                        Notification addNotification = notification.getValue(Notification.class);
                        notifications.addFirst(addNotification);
                        ((CustomAdapter)listAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;

    }
}
