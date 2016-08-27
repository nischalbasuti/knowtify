package com.hydratech19gmail.notify;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LoginActivity";

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

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

        setContentView(R.layout.activity_login);

        //...setting toolbar and tabs...
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        */

        //changing action bar title
        setTitle(R.string.title_activity_login);

        //set logo text view font
        TextView logo = (TextView) findViewById(R.id.logo_login);
        Typeface logo_typeface = Typeface.createFromAsset(getAssets(),"fonts/OliJo-Bold.ttf");
        logo.setTypeface(logo_typeface);

        //setting listener to check for sign in. Start main activity when successful.
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
                    finish();
                }
                else {
                    //user not signed in
                    Log.d(TAG,"not signed in");
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        //...google sign in...
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOptions)
                .build();
        Button googleSignInButton = (Button) findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this);
        //......

        //...email sign in...
        Button emailSignIn = (Button) findViewById(R.id.email_sign_in_button);
        emailSignIn.setOnClickListener(this);

        //...register new email...
        TextView register = (TextView) findViewById(R.id.tv_register);
        register.setOnClickListener(this);

        //....testing volley....
        Button volleyTest = (Button) findViewById(R.id.volley_test);
        volleyTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
            case R.id.tv_register:
           //     Intent httpIntent = new Intent(Intent.ACTION_VIEW);
           //     httpIntent.setData(Uri.parse("https://notify-1384.firebaseapp.com/"));
           //     startActivity(httpIntent);
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.email_sign_in_button:
                signIn();
                break;
            case R.id.volley_test:
                volleyTest();
                break;
        }
    }

    private void volleyTest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://hydra2622.appspot.com";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
        });

        requestQueue.add(stringRequest);
    }

    private void signIn() {
        TextView emailView = (TextView) findViewById(R.id.email);
        TextView passwordView = (TextView) findViewById(R.id.password);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        if (!validateEmailSignin(email,password)) {
            return;
        }
        try{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new
                    OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                //noinspection ThrowableResultOfMethodCallIgnored
                                Log.d(TAG,"sign in failed: "+task.getException());
                                Toast.makeText(getApplicationContext(),"sign in failed",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Log.d(TAG,"sign in successful");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateEmailSignin(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"must enter username and password",Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

    private void googleSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);

        Toast.makeText(this,"signing in...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //...successfully signed in...
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            if (googleSignInAccount != null) {
                Log.d(TAG,"google signed in as "+googleSignInAccount.getEmail());
            }
            if (googleSignInAccount != null) {
                Toast.makeText(this, "google signed in as "+googleSignInAccount.getDisplayName(), Toast
                        .LENGTH_SHORT).show();
            }
            firebaseAuthWithGoogle(googleSignInAccount);
        }
        else {
            //signed out
            Log.d(TAG,"google signed out "+result.getSignInAccount());
            Toast.makeText(this,"google signed out "+result,Toast.LENGTH_LONG).show();
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"failed to connect",Toast.LENGTH_LONG).show();
    }
}

