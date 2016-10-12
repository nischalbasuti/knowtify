package com.hydratech19gmail.notify;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by zappereton on 10/10/16.
 */

public class CustomDownloadDialogAdapter extends ArrayAdapter<Notification>     {
    LayoutInflater inflater;

    CustomDownloadDialogAdapter.ViewHolder mViewHolder;

    static class ViewHolder {
        public TextView nameOfFile;
        public TextView sizeOfFile;

        public ImageView download;

        public ProgressBar downloadProgressBar;
    }

    public CustomDownloadDialogAdapter(Context context, List<Notification> data){
        super(context,R.layout.home_fragment_row,data);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.download_dialog_row,parent,false);

            mViewHolder = new CustomDownloadDialogAdapter.ViewHolder();
            Log.d("uou","Entered");
            mViewHolder.nameOfFile = (TextView)convertView.findViewById(R.id.name_of_file);
            mViewHolder.sizeOfFile = (TextView)convertView.findViewById(R.id.size_of_file);

            mViewHolder.download = (ImageView)convertView.findViewById(R.id.download);
            mViewHolder.downloadProgressBar = (ProgressBar)convertView.findViewById(R.id.downloadProgressbar);

            mViewHolder.download.setTag(position+"d");
            mViewHolder.downloadProgressBar.setTag(position+"dpb");

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CustomDownloadDialogAdapter.ViewHolder) convertView.getTag();

            Log.d("CDownloadDialogAdapter","getView | not null");
        }

        Notification n = getItem(position);

        mViewHolder.nameOfFile.setText((position+1)+"."+n.getName());
        mViewHolder.sizeOfFile.setText("10Mb");
        mViewHolder.downloadProgressBar.setVisibility(View.INVISIBLE);
        mViewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
                parent.findViewWithTag(position+"d").setVisibility(View.INVISIBLE);
                parent.findViewWithTag(position+"dpb").setVisibility(View.VISIBLE);

                Notification innerN = getItem(position);
                String url = innerN.getName();
                ProgressBar pb = (ProgressBar) parent.findViewWithTag(position+"dpb");

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference fileRef = storageReference.child("images/unnamed.png");

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
                        ((ProgressBar) parent.findViewWithTag(position+"dpb")).setProgress(progressions);
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        parent.findViewWithTag(position+"dpb").setVisibility(View.INVISIBLE);                    }
                });

                //DownloadTask dt = new DownloadTask("download",position,pb);
                //dt.execute("https://firebasestorage.googleapis.com/v0/b/notify-1384.appspot.com/o/images%2Funnamed.png?alt=media&token=076de189-e55f-4213-8ef0-d20c9dd75797");
            }
        });

        mViewHolder.downloadProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
                parent.findViewWithTag(position+"d").setVisibility(View.VISIBLE);
                parent.findViewWithTag(position+"dpb").setVisibility(View.INVISIBLE);

                ProgressBar pb = (ProgressBar) parent.findViewWithTag(position+"dpb");
                //DownloadTask dt = new DownloadTask("not download",position,pb);
            }
        });
        return convertView;
    }
}


