package com.hydratech19gmail.notify;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "SearchFrag";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment,container,false);

        final LinkedList<Broadcast> broadcasts = new LinkedList<>();

        final ListAdapter listAdapter = new BroadcastAdapter(getContext(),broadcasts);
        ListView listView = (ListView) rootView.findViewById(R.id.searchList);
        listView.setAdapter(listAdapter);

        SearchView searchView = (SearchView)rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                broadcasts.clear();
                Log.d("Search",newText);
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").orderByChild("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        broadcasts.clear();

                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            String userKey = childSnapshot.getKey();
                            Log.d(TAG,"user key: "+userKey);

                            DatabaseReference broadcastRef = ref.child("users").child(userKey).child("broadcasts");
                            broadcastRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot d: dataSnapshot.getChildren()){
                                        Broadcast broadcast = d.getValue(Broadcast.class);
                                        broadcasts.add(broadcast);
                                        ((BroadcastAdapter)listAdapter).notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return false;
            }
        });

        listView.setOnItemClickListener(this);
        return rootView;

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(),BroadcastActivity.class);

        TextView broadcastName = (TextView) view.findViewById(R.id.broadcast_name);
        TextView broadcastInfo = (TextView) view.findViewById(R.id.broadcast_info);
        TextView privacy = (TextView) view.findViewById(R.id.privacy);
        String broadcasterKey = ((TextView) view.findViewById(R.id.userKey)).getText().toString();
        broadcasterKey = StringConverter.userIdToKey(broadcasterKey);

        Log.d(TAG,"broadcaster key: "+broadcasterKey);

        intent.putExtra("userId",broadcasterKey);

        intent.putExtra("broadcastName", broadcastName.getText().toString());
        intent.putExtra("broadcastInfo", broadcastInfo.getText().toString());
        intent.putExtra("privacy",privacy.getText().toString());

        startActivity(intent);
    }
}