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
                DownloadTask dt = new DownloadTask("download",position);
                dt.execute("https://firebasestorage.googleapis.com/v0/b/notify-1384.appspot.com/o/5%20Year%20IDP%20CSE%20-%202013.doc.pdf?alt=media&token=892a4a34-2cc6-4e62-b5a1-1a5f42acbf79");
            }
        });

        mViewHolder.downloadProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
                parent.findViewWithTag(position+"d").setVisibility(View.VISIBLE);
                parent.findViewWithTag(position+"dpb").setVisibility(View.INVISIBLE);

                DownloadTask dt = new DownloadTask("not download",position);
            }
        });
        return convertView;
    }
    class DownloadTask extends AsyncTask<String,Integer,Void>{

        String URL;
        int position;

        final String TAG = "DownloadTask";

        DownloadTask(String URL,int position){
            this.URL = URL;
            this.position = position;
        }
        @Override
        protected void onPreExecute() {
            Log.d(TAG, String.valueOf(position));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //mViewHolder.downloadProgressBar.set;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d(TAG,params[0].toString());
            int file_length=0;
            String path = params[0];
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                File folder = new File(getContext().getCacheDir(),"Notify");
                if (!(folder.exists())){
                    folder.mkdir();
                }

                File input_file = new File(folder,"file"+position+".pdf");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];

                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(folder);

                while ((count=inputStream.read())!=-1){
                    total += count;
                    outputStream.write(data,0,count);
                    int progress = (int)((total*100)/file_length);
                    publishProgress(progress);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}


