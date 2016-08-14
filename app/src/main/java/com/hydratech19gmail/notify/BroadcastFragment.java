package com.hydratech19gmail.notify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class BroadcastFragment extends Fragment {
    String path;
    TextView pathV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("something", DatabaseUtils.dumpCursorToString(cursor));
            int columnIndex = cursor.getColumnIndex(projection[0]);

            path = cursor.getString(columnIndex);

            Toast.makeText(getContext(),path,Toast.LENGTH_LONG).show();
            cursor.close();

            pathV.setText(path);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.broadcast_fragment,container,false);

        final EditText data1 = (EditText)rootView.findViewById(R.id.editText);
        final EditText data2 = (EditText)rootView.findViewById(R.id.editText2);
        final EditText data3 = (EditText)rootView.findViewById(R.id.editText3);

        Button sendB = (Button)rootView.findViewById(R.id.button);
        Button goGalleryB = (Button)rootView.findViewById(R.id.button2);

        pathV = (TextView)rootView.findViewById(R.id.textView);

        goGalleryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }

        });



        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageRef = storage.getReferenceFromUrl("gs://notify-1384.appspot.com");
                StorageReference mountainsRef = storageRef.child("mountains.jpg");
                StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

                mountainsRef.getName().equals(mountainImagesRef.getName());
                mountainsRef.getPath().equals(mountainImagesRef.getPath());

                Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

                Toast.makeText(getContext(),"file ",Toast.LENGTH_LONG).show();
                Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");
                Notification notification = new Notification(data1.getText().toString(),data2
                        .getText().toString(),data3.getText().toString());

                ref.push().setValue(notification);
                data1.setText("");
                data2.setText("");

            }
        });
        return rootView;
    }
}
