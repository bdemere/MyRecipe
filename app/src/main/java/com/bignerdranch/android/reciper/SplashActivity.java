package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 *  Splash page activity
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 12/02/15.
 */
public class SplashActivity extends AppCompatActivity {

    // duration of wait
    private final int SPLASH_DURATION = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // handler starts the homepage and close the splash-screen after some seconds.
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // create intent to start homepage activity
                Intent mainIntent = new Intent(SplashActivity.this,HomePageActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
    }
}