package com.hydratech19gmail.notify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Created by Jaelse on 30-07-2016.
 */
public class BroadcastFragment extends Fragment implements View.OnClickListener, OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "BroadcastFragmen";

    private FirebaseUser user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.broadcast_fragment,container,false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        final LinkedList<Broadcast> broadcasts = new LinkedList<>();
        final ListAdapter listAdapter = new BroadcastAdapter(this.getContext(),broadcasts);
        final ListView listView = (ListView) rootView.findViewById(R.id.broadcast_list);

        listView.setAdapter(listAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference broadcastRef = ref.child("users/"+user.getUid()+"/broadcasts");

        broadcastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO find a better fix
                broadcasts.clear();

                for (DataSnapshot broadcast : dataSnapshot.getChildren()) {
                    try {
                        Broadcast addBroadcast = broadcast.getValue(Broadcast.class);
                        broadcasts.addFirst(addBroadcast);
                        ((BroadcastAdapter) listAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //on  item click
        listView.setOnItemClickListener(this);


        //showing dropdown on long click
        listView.setOnItemLongClickListener(this);

        //new broadcast
        FloatingActionButton fabNewBroadcast = (FloatingActionButton) rootView.findViewById(R.id.fab_new_broadcast);
        fabNewBroadcast.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(),BroadcastActivity.class);

        TextView broadcastName = (TextView) view.findViewById(R.id.broadcast_name);
        TextView broadcastInfo = (TextView) view.findViewById(R.id.broadcast_info);
        TextView privacy = (TextView) view.findViewById(R.id.privacy);
        String broadcasterKey = ((TextView) view.findViewById(R.id.userKey)).getText().toString();

        Log.d(TAG,"broadcaster key: "+broadcasterKey);

        intent.putExtra("userId",broadcasterKey);

        intent.putExtra("broadcastName", broadcastName.getText().toString());
        intent.putExtra("broadcastInfo", broadcastInfo.getText().toString());
        intent.putExtra("privacy",privacy.getText().toString());

        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> mDropDownList;
        mDropDownList = new ArrayList<>();
        mDropDownList.add("Settings");
        mDropDownList.add("Delete");


        final PopupWindowDropDownMenu popupWindowDropDownMenu = new PopupWindowDropDownMenu(getContext(),mDropDownList);

        PopupWindow popupWindow = popupWindowDropDownMenu.popupWindowDropDownMenu();
        popupWindow.showAsDropDown(
                view,
                view.getWidth()/2 - popupWindow.getWidth()/2,
                -view.getHeight() - popupWindow.getHeight()/2
        );

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_broadcast:
                NewBroadcastDialog newBroadcastDialog = new NewBroadcastDialog(getContext(),user);
                newBroadcastDialog.show();
                break;
        }
    }
}
