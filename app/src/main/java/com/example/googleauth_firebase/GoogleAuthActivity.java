package com.example.googleauth_firebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleAuthActivity extends AppCompatActivity {

    Button sign_btn;

    private static final int REQ_ONE_TAP = 1;  // Can be any integer unique to the Activity.

    BeginSignInRequest begin_sign_in_request;
    private SignInClient one_tap_client;
    private FirebaseAuth auth;
    // Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);

                auth = FirebaseAuth.getInstance();
                sign_btn = findViewById(R.id.signup_option_2);

                sign_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signup();
                    }
                });

    }

    private void signup(){
        // trigger One Tap Sign-In
        one_tap_client = Identity.getSignInClient(this);

        // configures the BeginSignInRequest object, then calls setGoogleIdTokenRequestOptions
        begin_sign_in_request = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        if(one_tap_client != null) {
            one_tap_client.beginSignIn(begin_sign_in_request)
                    .addOnSuccessListener(GoogleAuthActivity.this, result -> {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP,
                                    null,  // fill in null or appropriate extras
                                    0, 0, 0
                            );
                        } catch (Exception e) {
                            Log.e(TAG, "Error starting One Tap Sign-In: ", e);
                        }
                    })
                    .addOnFailureListener(GoogleAuthActivity.this, e -> {
                        Log.e(TAG, "One Tap Sign-In failed: ", e);
                        Toast.makeText(GoogleAuthActivity.this, "Sign-In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void firebaseAuthWithGoogle(@Nullable Intent data){
        try {
            SignInCredential googleCredential = one_tap_client.getSignInCredentialFromIntent(data);
            String idToken = googleCredential.getGoogleIdToken();
            if (idToken !=  null) {
                // got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = auth.getCurrentUser();

                                    // if sign up or sign in successful, update UI to home activity
                                    updateUI(user);
                                } else {
                                    // if sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                }
                            }
                        });
            }
            Log.d(TAG, "Got ID token.");
        } catch (ApiException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            firebaseAuthWithGoogle(data);
        }
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            startActivity(new Intent(GoogleAuthActivity.this, HomeActivity.class));
            finish();
        }
    }

}