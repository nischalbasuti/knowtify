package com.hydratech19gmail.notify;

import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;
import static com.hydratech19gmail.notify.MainActivity.NOTIFICATIONS;

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

        //taking the static linked list NOTIFICATIONS
        final ListAdapter listAdapter = new CustomAdapter(this.getContext(),NOTIFICATIONS);
        final ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);


        //listen for new notifications
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        String prefUserKey = sharedPreferences.getString("user_key", "user key doesnt exits");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference notificationRef = ref.child("users")
                .child(prefUserKey)
                .child("newNotification");

        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notification : dataSnapshot.getChildren()) {
                    try {
                        Log.d("child notifications", notification.toString());
                        /*add these notifications to the local database*/
                        DatabaseOperations dop = new DatabaseOperations(getContext());
                        Notification newNotification = notification.getValue(Notification.class);
                        dop.putNotification(dop, newNotification);

                        NOTIFICATIONS.addFirst(newNotification);
                        ((CustomAdapter)listAdapter).notifyDataSetChanged();
                        DatabaseReference oldNotificationRef = notificationRef.child(notification.getKey());
                        oldNotificationRef.removeValue();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
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
