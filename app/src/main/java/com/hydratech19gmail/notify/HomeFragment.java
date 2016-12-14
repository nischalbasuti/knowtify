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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

/*
 * Created by Jaelse on 30-07-2016.
 */
public class HomeFragment extends Fragment{
    ListAdapter listAdapter;
    private static final String TAG = "HomeFrgment";

    int firstTime = 1;

    DatabaseReference notificationRef;
    ValueEventListener valueEventListener;

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

        //taking the static linked list NOTIFICATIONS
        listAdapter = new CustomAdapter(this.getContext(),NOTIFICATIONS);
        final ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);

        Log.d("HomeFragment","CreateView");

        if(firstTime == 1){
            UpdateNotificationsTask updateNotificationsTask = new UpdateNotificationsTask(getContext(),listAdapter);
            updateNotificationsTask.execute("addAndSort");

        }
        firstTime = 0;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(WholeNotificationActivity.CHECK){
            refresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void refresh(){
        WholeNotificationActivity.CHECK = false;
        ((CustomAdapter)listAdapter).refresh();
    }
}
