package com.hydratech19gmail.notify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/*
 * Created by Jaelse on 30-07-2016.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    private static final String TAG = "SearchFrag";

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final LinkedList<SearchResult> searchResult = new LinkedList<>();


    ImageView broadcast_search;
    ImageView user_search;
    ImageView default_search;

    boolean do_broadcast_search;
    boolean do_user_search;
    boolean do_both_search;

    public SearchFragment(){

        do_broadcast_search = true;
        do_user_search = false;
        do_both_search = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.search_fragment,container,false);

        broadcast_search = (ImageView)rootView.findViewById(R.id.broadcast_search);
        user_search = (ImageView)rootView.findViewById(R.id.user_search);
        default_search = (ImageView)rootView.findViewById(R.id.default_search);

        broadcast_search.setOnClickListener(this);
        user_search.setOnClickListener(this);
        default_search.setOnClickListener(this);




        final ListAdapter searchResultAdapter = new SearchResultAdapter(getContext(),searchResult);
        final ListView listView = (ListView) rootView.findViewById(R.id.searchList);
        listView.setAdapter(searchResultAdapter);

        SearchView searchView = (SearchView)rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.d("Search",newText);
                if(do_broadcast_search){


                }
                else if(do_user_search){
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference searchRef = ref.child("search");
                    searchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            searchResult.clear();
                            for(DataSnapshot d: dataSnapshot.getChildren()) {
                                DatabaseReference userRef = ref.child("users").child(d.getKey());

                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot userDataSnapshot: dataSnapshot.getChildren()){
                                            String userName = userDataSnapshot.child("username").getValue().toString();

                                            if(userName.contains(newText)){
                                                SearchResult userResult = dataSnapshot.getValue(SearchResult.class);

                                                searchResult.add(userResult);
                                                ((BroadcastAdapter)searchResultAdapter).notifyDataSetChanged();
                                            }


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
                }
                return false;
            }
        });

        listView.setOnItemClickListener(this);
        return rootView;

    }

    public void searchListener(){
        DatabaseReference searchRef = ref.child("search");
        searchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchResult.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    for(DataSnapshot b: d.child("broadcasts").getChildren()){
                        Log.d("search",b.getKey());
                         //   broadcastListener();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void broadcastListener(String uKey,String bKey,final String newText){
        DatabaseReference broadcastRef = ref.child("users")
                .child(uKey)
                .child("broadcasts")
                .child(bKey);
        broadcastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String broadcastName = dataSnapshot.child("name").getValue().toString();
                Log.d("Search",broadcastName);
                if(broadcastName.contains(newText)){
                    SearchResult result = new SearchResult(dataSnapshot.child("name").getValue().toString());
                    searchResult.add(result);
                   // ((SearchResultAdapter)searchResultAdapter).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       /* Intent intent = new Intent(getContext(),BroadcastActivity.class);

        TextView broadcastName = (TextView) view.findViewById(R.id.query_subject);
        TextView broadcastInfo = (TextView) view.findViewById(R.id.broadcast_info);
        TextView privacy = (TextView) view.findViewById(R.id.privacy);
        String broadcasterKey = ((TextView) view.findViewById(R.id.userKey)).getText().toString();
        broadcasterKey = StringConverter.userIdToKey(broadcasterKey);

        Log.d(TAG,"broadcaster key: "+broadcasterKey);

        intent.putExtra("userId",broadcasterKey);

        intent.putExtra("broadcastName", broadcastName.getText().toString());
        intent.putExtra("broadcastInfo", broadcastInfo.getText().toString());
        intent.putExtra("privacy",privacy.getText().toString());

        startActivity(intent);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.broadcast_search:
                do_broadcast_search = true;
                do_user_search = false;
                do_both_search = false;
                break;
            case R.id.user_search:
                do_broadcast_search = false;
                do_user_search = true;
                do_both_search = false;
                break;
            case R.id.default_search:
                do_broadcast_search = false;
                do_user_search = false;
                do_both_search = true;
                break;
        }
    }
}