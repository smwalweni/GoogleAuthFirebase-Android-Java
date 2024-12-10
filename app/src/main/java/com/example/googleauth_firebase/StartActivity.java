package com.example.googleauth_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // updates UI to the next activity, after a delay of 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, 3000);
    }

    private void updateUI() {
        intent = new Intent(StartActivity.this,GoogleAuthActivity.class);
        startActivity(intent);
        finish();
    }
}