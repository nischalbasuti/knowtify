package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

/**
 * Created by zappereton on 10/10/16.
 */

public class CustomDownloadDialogAdapter extends ArrayAdapter<Notification>{
    LayoutInflater inflater;

    CustomDownloadDialogAdapter.ViewHolder mViewHolder;

    StorageReference storageReference;

    static class ViewHolder {
        public TextView nameOfFile;
        public TextView sizeOfFile;

        public ImageView download;
        public ImageView openFolder;
        public ProgressBar downloadProgressBar;
    }

    public CustomDownloadDialogAdapter(Context context, List<Notification> data,StorageReference storageReference){
        super(context,R.layout.home_fragment_row,data);
        inflater = LayoutInflater.from(context);

        this.storageReference = storageReference;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.download_dialog_row,parent,false);

            mViewHolder = new CustomDownloadDialogAdapter.ViewHolder();
            mViewHolder.nameOfFile = (TextView)convertView.findViewById(R.id.name_of_file);
            mViewHolder.sizeOfFile = (TextView)convertView.findViewById(R.id.size_of_file);

            mViewHolder.download = (ImageView)convertView.findViewById(R.id.download);
            mViewHolder.downloadProgressBar = (ProgressBar)convertView.findViewById(R.id.downloadProgressbar);
            mViewHolder.openFolder = (ImageView)convertView.findViewById(R.id.open_folder);

            mViewHolder.downloadProgressBar.setProgress(0);
            mViewHolder.downloadProgressBar.setMax(100);

            mViewHolder.download.setTag(position+"d");
            mViewHolder.openFolder.setTag(position+"of");
            mViewHolder.downloadProgressBar.setTag(position+"dpb");
            mViewHolder.sizeOfFile.setTag(position+"size");

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CustomDownloadDialogAdapter.ViewHolder) convertView.getTag();
        }

        Notification n = getItem(position);

        mViewHolder.nameOfFile.setText((position+1)+"."+n.getName());

        mViewHolder.downloadProgressBar.setVisibility(View.INVISIBLE);
        mViewHolder.openFolder.setVisibility(View.INVISIBLE);

        mViewHolder.downloadProgressBar.setProgress(50);

        final StorageReference fileRef = storageReference.child("images/Screenshot_2016-08-10-11-48-52_1.jpg");

        mViewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
                parent.findViewWithTag(position+"d").setVisibility(View.INVISIBLE);
                parent.findViewWithTag(position+"of").setVisibility(View.INVISIBLE);
                parent.findViewWithTag(position+"dpb").setVisibility(View.VISIBLE);
                ((TextView)parent.findViewWithTag(position+"size")).setText("0");


                File folder = new File("/sdcard/Notify/");
                if (!(folder.exists())){
                    folder.mkdirs();
                }

                File input_file = new File(folder,"file"+position+".png");

                final double[] size = {0};

                fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        size[0] = storageMetadata.getSizeBytes();
                    }
                });
                fileRef.getFile(input_file).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        int progressions = (int)(taskSnapshot.getBytesTransferred()*100/size[0]);

                        Log.d("progressions", String.valueOf(progressions));
                        ((TextView)parent.findViewWithTag(position+"size")).setText(String.valueOf(progressions)+"%");

                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        parent.findViewWithTag(position+"dpb").setVisibility(View.INVISIBLE);
                        parent.findViewWithTag(position+"of").setVisibility(View.VISIBLE);
                        //need to correct this thing.
                        ((TextView) parent.findViewWithTag(position+"size")).setText((float) ((size[0])/(1024))+"mb");
                    }
                });
            }
        });

        mViewHolder.downloadProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
                parent.findViewWithTag(position+"d").setVisibility(View.VISIBLE);
                parent.findViewWithTag(position+"dpb").setVisibility(View.INVISIBLE);

                fileRef.getActiveDownloadTasks().get(0).cancel();
            }
        });

        mViewHolder.openFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opening....",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(),ShowAttachmentImage.class);

                intent.putExtra("url","/sdcard/Notify/file0.png");

                getContext().startActivity(intent);

            }
        });
        return convertView;
    }


}


