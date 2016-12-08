package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by nischal on 15/9/16.
 */
public class QueryAdapter extends ArrayAdapter<QueryObject> {
    static final String TAG = "QueryAdapter";

    LayoutInflater mLayoutInflater;

    ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView querySubject;
        public TextView queryContent;
        public TextView queryUserName;
        public ImageView dropDown;

        public TextView queryKey;
    }

    public String notificationKey;
    public String broadcastUserKey;
    public String broadcastKey;
    public String queryKey;

    ArrayList<String> dropDownList = new ArrayList<>();

    public QueryAdapter(Context context, List<QueryObject> resource) {
        super(context, R.layout.query_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

        dropDownList.add("hide");
        dropDownList.add("remove");
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.query_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.querySubject = (TextView) convertView.findViewById(R.id.query_subject);
            mViewHolder.queryContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.queryUserName = (TextView) convertView.findViewById(R.id.query_userid);
            mViewHolder.dropDown = (ImageView) convertView.findViewById(R.id.dropDownMenu);

            mViewHolder.queryKey = (TextView) convertView.findViewById(R.id.queryKey);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        QueryObject query = getItem(position);

        notificationKey = query.getNotificationKey();
        broadcastUserKey = query.getBroadcastUserKey();
        broadcastKey = query.getBroadcastKey();
        queryKey = query.getQueryKey();

        mViewHolder.queryKey.setText(queryKey);

        mViewHolder.querySubject.setText(query.getQuerySubject());
        mViewHolder.queryContent.setText(query.getQueryContent());


        final String[] username = new String[1];
        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(query.getQueryUserKey()).child("username");
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username[0] = dataSnapshot.getValue().toString();
                mViewHolder.queryUserName.setText(username[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mViewHolder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"answer", "delete"};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(getContext(),"answer",Toast.LENGTH_SHORT).show();
                        } else if (which == 1) {
                            Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });
        return convertView;
    }
}
