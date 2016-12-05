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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference subscriptionRef = ref.child("users")
                                            .child(user.getUid())
                                            .child("subscriptions");

        final LinkedList<String> subscriptionKeyList = new LinkedList<>();
        subscriptionRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot subscriptionChannel : dataSnapshot.getChildren()) {
                    try{
                        Subscription subscription = subscriptionChannel.getValue(Subscription.class);
                        Log.d("HomeFragment","Entered");
                        if(subscriptionKeyList.isEmpty() || !(subscriptionKeyList.isEmpty())){
                            subscriptionKeyList.addLast(subscriptionChannel.getKey());
                            Log.d("HomeFragment","taking list again");
                            NotificationsListener notificationsListener = new NotificationsListener(listAdapter,getContext(),subscription.getSubscribersKey(),subscriptionChannel.getKey());
                            notificationsListener.getNotifications();
                        }
                    }catch (Exception e){
                        Log.d(TAG, e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });/*
        notificationRef = ref.child("users")
                .child(user.getUid())
                .child("newNotification");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notification : dataSnapshot.getChildren()) {
                    try {
                        Notification newNotification = notification.getValue(Notification.class);
                        //add these notifications to the local database
                        Log.d("Home time", "jaekse" + newNotification.getTimeStamp());
                        DatabaseOperations dop = new DatabaseOperations(getContext());
                        dop.putNotification(dop, newNotification);

                        NOTIFICATIONS.addFirst(newNotification);
                        ((CustomAdapter) listAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };*/
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("home fragment","onResume");
        //add the listener
        //notificationRef.addValueEventListener(valueEventListener);

        if(WholeNotificationActivity.CHECK == true){
            refresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //remove the listener
        //notificationRef.removeEventListener(valueEventListener);
    }

    public void refresh(){
        WholeNotificationActivity.CHECK = false;
        ((CustomAdapter)listAdapter).refresh();
    }
}
