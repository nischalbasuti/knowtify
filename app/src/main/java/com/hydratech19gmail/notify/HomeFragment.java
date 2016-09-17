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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.LinkedList;
/*
 * Created by Jaelse on 30-07-2016.
 */
public class HomeFragment extends Fragment{

    private static final String TAG = "HomeFrgment";

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO The listview should not always load.

        View rootView = inflater.inflate(R.layout.home_fragment,container,false);

        final LinkedList<Notification> notifications = new LinkedList<>();
        final ListAdapter listAdapter = new CustomAdapter(this.getContext(),notifications);
        final ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);

        //listen for new notifications
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference();
        DatabaseReference notificationRef = ref.child("notifications");
        notificationRef.addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
