package com.github.dual_navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    // mHandler is used for postdelayed
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Set title
        setTitle("Dual NavDrawer");

        // Set delay duration before going to main menu
        int secondsDelayed = 2;

        // Run handler post delayed
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed*1000);
    }

    // Disable back button
    @Override
    public void onBackPressed(){

    }
}
