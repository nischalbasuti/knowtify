package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
class QueryAdapter extends ArrayAdapter<QueryObject> {
    static final String TAG = "QueryAdapter";

    private LayoutInflater mLayoutInflater;

    private ViewHolder mViewHolder;

    static class ViewHolder {
        TextView querySubject;
        TextView queryContent;
        TextView queryUserName;
        ImageView dropDown;
        ImageView rateUp;
        TextView rateValue;

        TextView queryKey;
    }

    int rating;
    //private ArrayList<String> dropDownList = new ArrayList<>();

    public QueryAdapter(Context context, List<QueryObject> resource) {
        super(context, R.layout.query_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

      //  dropDownList.add("hide");
     //   dropDownList.add("remove");
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
            mViewHolder.rateUp = (ImageView) convertView.findViewById(R.id.rateUp);
            mViewHolder.rateValue = (TextView) convertView.findViewById(R.id.rateValue);

            mViewHolder.queryKey = (TextView) convertView.findViewById(R.id.queryKey);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        final QueryObject query = getItem(position);

        String notificationKey = query.getNotificationKey();
        String broadcastUserKey = query.getBroadcastUserKey();
        String broadcastKey = query.getBroadcastKey();

        final String queryKey = query.getQueryKey();
        mViewHolder.queryKey.setText(queryKey);

        mViewHolder.rateValue.setText(Integer.toString(query.getRating()));
        mViewHolder.querySubject.setText(query.getQuerySubject());
        mViewHolder.queryContent.setText(query.getQueryContent());


        final String[] username = new String[1];
        final DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference()
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
        final DatabaseReference queryRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(query.getBroadcastUserKey())
                .child("broadcasts").child(query.getBroadcastKey())
                .child("notifications").child(query.getNotificationKey())
                .child("queries").child(query.getQueryKey());

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                mViewHolder.rateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child("ratings")
                        .orderByChild(StringConverter.removeBadChars(queryRef.toString()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                DataSnapshot status = dataSnapshot.child(StringConverter.removeBadChars(queryRef.toString()));

                                Log.d(TAG,"status : "+status.getValue());
                                rating = 1;
                                String statusText;
                                try{
                                    statusText = status.getValue().toString();
                                } catch (Exception e){
                                    e.printStackTrace();
                                    statusText = "-1";
                                }
                                if(statusText == "1"){
                                    rating = -1;

                                } else {
                                    rating = 1;
                                }
                                queryRef.child("rating").setValue(query.getRating()+rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(),"rate "+query.getQuerySubject(),Toast.LENGTH_SHORT).show();
                                        userRef.child("ratings").child(StringConverter.removeBadChars(queryRef.toString())).setValue(rating)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getContext(),"rating update ",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        return convertView;
    }
}
