package com.hydratech19gmail.notify;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //user is signed in
                    Log.d(TAG,"signed in");
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
                signUp();
                break;
        }
    }

    private void signUp() {

        TextView emailView = (TextView) findViewById(R.id.email);
        TextView passwordView = (TextView) findViewById(R.id.password);
        TextView passwordCheckView = (TextView) findViewById(R.id.password_check);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordCheck = passwordCheckView.getText().toString();

        //TODO
        //add more verificatoin stuff
        if(!verifyRegistration(email,password,passwordCheck)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.d(TAG,""+task.getException());
                    Toast.makeText(SignupActivity.this,"signup failed, try again", Toast
                            .LENGTH_LONG).show();
                    displayError(task.getException().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(),"sign up successfull",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private boolean verifyRegistration(String email, String password, String passwordCheck) {
        if(email.isEmpty()) {
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

        return true;
    }

    private void displayError(String errorText) {
        TextView errorView = (TextView) findViewById(R.id.errorText);
        errorView.setText(errorText);
    }
}

