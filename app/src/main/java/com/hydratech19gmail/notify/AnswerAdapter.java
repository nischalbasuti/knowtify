package com.hydratech19gmail.notify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by nischal on 8/12/16.
 */
public class AnswerAdapter extends ArrayAdapter<Answer>{

    private static final String TAG = "AnswrAdapter";
    class ViewHolder{

        public TextView answerContent;
        public TextView userId;
        public TextView time;
    }

    ViewHolder mViewHolder;
    LayoutInflater mLayoutInflater;

    public AnswerAdapter(Context context, List<Answer> resource) {
        super(context, R.layout.answer_item,resource);
        mLayoutInflater = LayoutInflater.from(getContext());

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.answer_item,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.answerContent = (TextView) convertView.findViewById(R.id.answer_content);
            mViewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
            mViewHolder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(mViewHolder);

            Log.d(TAG, "getView, convertView | null");
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView, convertView | not null");
        }

        Answer answer = getItem(position);

        mViewHolder.answerContent.setText(answer.getContent());
        mViewHolder.userId.setText(answer.getUserKey());

        mViewHolder.time.setText(DateConvert.timeStampToDate(Long.parseLong(answer.getTimeStamp())*1000));

        return convertView;
    }
}
