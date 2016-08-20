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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class BroadcastFragment extends Fragment {
    String path;
    TextView pathV;

    private FirebaseUser user;
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
/*
       // final EditText data1 = (EditText)rootView.findViewById(R.id.editText);
       // final EditText data2 = (EditText)rootView.findViewById(R.id.editText2);
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



        user = FirebaseAuth.getInstance().getCurrentUser();

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploading file
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageRef = storage.getReferenceFromUrl("gs://notify-1384.appspot.com");
                StorageReference mountainsRef = storageRef.child("mountains.jpg");
                StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

                mountainsRef.getName().equals(mountainImagesRef.getName());
                mountainsRef.getPath().equals(mountainImagesRef.getPath());

                try{

                    Uri file = Uri.fromFile(new File(path));
                    StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("BroadcastFragment: ","file thing: "+exception.getMessage());
                            Toast.makeText(getContext(),"error uploading file",Toast.LENGTH_LONG).show();
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
                } catch (Exception e) {
                    Log.d("BroadcastFragment: ","file thing: "+e.getMessage());
                    e.printStackTrace();
                }

                Toast.makeText(getContext(),"file ",Toast.LENGTH_LONG).show();

                //sending message to database
                Firebase ref = new Firebase("https://notify-1384.firebaseio.com/");
                Notification notification = new Notification(
                                                            user.getDisplayName(),
                                                            user.getEmail(),
                                                            data3.getText().toString()
                                                            );
                ref.push().setValue(notification);

                //resetting fields to blank
                //data1.setText("");
                //data2.setText("");
                data3.setText("");

            }
        });
*/

        final List<Broadcast> broadcasts = new LinkedList<>();

        final ListAdapter listAdapter = new BroadcastAdapter(this.getContext(),broadcasts);
        final ListView listView = (ListView) rootView.findViewById(R.id.broadcast_list);
        listView.setAdapter(listAdapter);


        Firebase firebase = new Firebase("https://notify-1384.firebaseio.com/");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(),"broadcast update",Toast.LENGTH_SHORT).show();

                for (DataSnapshot broadcast : dataSnapshot.getChildren()) {
                    try {
                        Broadcast addBroadcast = broadcast.getValue(Broadcast.class);
                        if(addBroadcast.getPrivacy() != null) {
                            broadcasts.add(addBroadcast);
                            ((BroadcastAdapter) listAdapter).notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //on  item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getContext(),BroadcastActivity.class);

                TextView broadcastName = (TextView) view.findViewById(R.id.broadcast_name);
                TextView broadcastInfo = (TextView) view.findViewById(R.id.broadcast_info);

                intent.putExtra("broadcastName", broadcastName.getText().toString());
                intent.putExtra("broadcastInfo", broadcastInfo.getText().toString());

                startActivity(intent);
            }
        });


        FloatingActionButton fabNewBroadcast = (FloatingActionButton) rootView.findViewById(R.id.fab_new_broadcast);
        fabNewBroadcast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NewBroadcastDialog newBroadcastDialog = new NewBroadcastDialog(getContext());
                newBroadcastDialog.show();

                //TODO find a better fix
                broadcasts.clear();
            }
        });
        return rootView;
    }
}
