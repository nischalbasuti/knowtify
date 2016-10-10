package com.hydratech19gmail.notify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zappereton on 10/10/16.
 */

public class CustomDownloadDialogAdapter extends ArrayAdapter<Notification> {
    LayoutInflater inflater;

    CustomDownloadDialogAdapter.ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView nameOfFile;
        public TextView sizeOfFile;

        public ImageView download;
    }

    public CustomDownloadDialogAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.download_dialog_row,parent,false);

            mViewHolder = new CustomDownloadDialogAdapter.ViewHolder();
            Log.d("uou","Entered");
            mViewHolder.nameOfFile = (TextView)convertView.findViewById(R.id.name_of_file);
            mViewHolder.sizeOfFile = (TextView)convertView.findViewById(R.id.size_of_file);

            mViewHolder.download = (ImageView)convertView.findViewById(R.id.download);

            convertView.setTag(mViewHolder);

            Log.d("CDownloadDialogAdapter","getView | null");
        } else {
            mViewHolder = (CustomDownloadDialogAdapter.ViewHolder) convertView.getTag();

            Log.d("CDownloadDialogAdapter","getView | not null");
        }


        Notification n = getItem(position);

        mViewHolder.nameOfFile.setText("The World on fire");
        mViewHolder.sizeOfFile.setText("10Mb");

        return convertView;
    }
}
