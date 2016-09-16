package com.hydratech19gmail.notify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/*
 * Created by nischal on 25/8/16.
 */
public class NewNotificationDialog extends Activity implements View.OnClickListener {

    FirebaseUser mUser;

    private EditText notificationName;
    private EditText notificationSubject;
    private EditText notificationContent;

 //   public NewNotificationDialog(Context context, FirebaseUser user) {
 //       this.mUser = user;
 //   }

    //...................



    String path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("something", DatabaseUtils.dumpCursorToString(cursor));
            int columnIndex = cursor.getColumnIndex(projection[0]);

            path = cursor.getString(columnIndex);

            Toast.makeText(this,path,Toast.LENGTH_LONG).show();
            cursor.close();

            //uploading file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://notify-1384.appspot.com");
            try{

                Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("NotifDialog: ","file thing: "+exception.getMessage());
                        Toast.makeText(getApplicationContext(),"error uploading file",Toast.LENGTH_LONG).show();
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
                Log.d("NotifDialog: ","file thing: "+e.getMessage());
                //e.printStackTrace();
            }
        }
    }


    //.......................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_notification);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        notificationName = (EditText) findViewById(R.id.notification_name);
        notificationSubject = (EditText) findViewById(R.id.notification_subject);
        notificationContent = (EditText) findViewById(R.id.notification_content);

        ImageView uploadAttachButton = (ImageView) findViewById(R.id.upload_attachments_button);
        uploadAttachButton.setOnClickListener(this);

        Button newNotificationButton = (Button) findViewById(R.id.new_notification_button);
        newNotificationButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.new_notification_exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload_attachments_button:
                uploadAttachments();
                break;
            case R.id.new_notification_button:
                makeNewNotification();
                break;
            case R.id.new_notification_exit_button:
                onBackPressed();
                break;
        }
    }

    private void makeNewNotification() {
       DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
       DatabaseReference notificationRef = ref.child("notifications");

        Long tsLong = System.currentTimeMillis()/1000;

        String timeStamp = tsLong.toString();
        String name = notificationName.getText().toString();
        String subject = notificationSubject.getText().toString();
        String content = notificationContent.getText().toString();

        if (name.isEmpty() || subject.isEmpty()) {
            Toast.makeText(this,"must enter name and subject",Toast.LENGTH_SHORT).show();
        } else {
            Notification notification = new Notification(
                    name,
                    subject,
                    content,
                    timeStamp
            );
           notificationRef.push().setValue(notification);
            onBackPressed();
        }
    }

    private void uploadAttachments() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }
}
