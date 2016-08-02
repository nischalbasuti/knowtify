package com.hydratech19gmail.notify;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        View rootView = inflater.inflate(R.layout.home_fragment,container,false);
        String[] notifications = {"Jaelse","Nischal","JNTU","CSE","ECE","ME","CE"};

        ListAdapter listAdapter = new CustomAdapter(this.getContext(),notifications);
        ListView listView = (ListView) rootView.findViewById(R.id.notificationList);
        listView.setAdapter(listAdapter);

        return rootView;

    }
}
