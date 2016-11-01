package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class SignupActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private String displayName;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prevent keyboard from opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_signup);

        setTitle(R.string.title_activity_signup);

        //set logo text view font
        TextView logo = (TextView) findViewById(R.id.logo_login);
        Typeface logo_typeface = Typeface.createFromAsset(getAssets(),"fonts/OliJo-Bold.ttf");
        logo.setTypeface(logo_typeface);

        //checking if signed in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //user is signed in
                    Log.d(TAG,"signed in");

                    //set display name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG,"display name updated to "+displayName);
                            }
                            else{
                                //noinspection ThrowableResultOfMethodCallIgnored
                                Log.d(TAG,"display name update failed: "+task.getException());
                            }
                            //pushing token
                            pushToken(user);
                        }
                    });

                    //start main acivity
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(intent);
                }
                else {
                    //user not signed in
                    Log.d(TAG,"not signed in");
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        Button signupButton = (Button) findViewById(R.id.email_sign_up_button);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_up_button:
                displayError("");
                signUp();
                break;
        }
    }

    private void signUp() {

        TextView displayNameView = (TextView) findViewById(R.id.display_name);
        TextView emailView = (TextView) findViewById(R.id.email);
        TextView passwordView = (TextView) findViewById(R.id.password);
        TextView passwordCheckView = (TextView) findViewById(R.id.password_check);

        String displayName = displayNameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordCheck = passwordCheckView.getText().toString();

        //TODO
        //add more verification stuff
        if(!verifyRegistration(displayName,email,password,passwordCheck)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {
            @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.d(TAG,""+task.getException());
                    Toast.makeText(SignupActivity.this,"sign up failed, try again", Toast
                            .LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"sign up successful",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean verifyRegistration(String displayName,String email, String password, String
            passwordCheck) {
        if(displayName.isEmpty()) {
            displayError("enter name");
            return false;
        }
        else if(email.isEmpty()) {
            displayError("enter email");
            return false;
        }
        else if(password.isEmpty()) {
            displayError("enter password");
            return false;
        }
        else if(passwordCheck.isEmpty()) {
            displayError("confirm your password");
            return false;
        }
        else if(!Objects.equals(password, passwordCheck)) {
            displayError("passwords do not match");
            return false;
        }

        this.displayName = displayName;
        return true;
    }

    private void displayError(String errorText) {
        TextView errorView = (TextView) findViewById(R.id.errorText);
        errorView.setText(errorText);
    }


    static String token;
    public static void getToken(String t){
        Log.d(TAG,"get token: "+t);
        token = t;
    }

    static String staticKey;
    String prefToken;
    boolean userExists = false;

    private void pushToken(final FirebaseUser user) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = ref.child("users");

        //getting token value and pushing
        SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        prefToken = sharedPref.getString("device_token","device_token_doesnt_exist");

        //TODO send request to remove duplicate tokens

        //checking for email
        ref.equalTo(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            String key = childSnapshot.getKey();
                            Log.d(TAG,"user key: "+key);

                            userExists = true;
                            //old user new/old device
                            ref.child("users/"+user.getUid()).setValue(prefToken);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if(!userExists){
            //creating user for first time
            if(user.getPhotoUrl() == null) {
                ref.child("users/")
                        .child(user.getUid())
                        .setValue(new User(user.getUid(),
                                prefToken,
                                user.getEmail(),
                                user.getDisplayName(),
                                "photurl is empty"
                        ));
            }
            else{
                ref.child("users/")
                        .child(user.getUid())
                        .setValue(new User(user.getUid(),
                                prefToken,
                                user.getEmail(),
                                user.getDisplayName(),
                                user.getPhotoUrl().toString()
                        ));
            }
        }



        userRef.orderByChild("token")
                .equalTo(token)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            String key = childSnapshot.getKey();
                            Log.d(TAG,"user key: "+key);
                            staticKey = key;

                            //writting key value pair
                            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("myprefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("user_key", key);
                            editor.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
