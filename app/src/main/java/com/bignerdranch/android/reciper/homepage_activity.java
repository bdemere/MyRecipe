package com.bignerdranch.android.reciper;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by bubujay on 11/15/15.
 */
public class homepage_activity extends AppCompatActivity {
    private TextView mReciper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        Typeface roboto = Typeface.createFromAsset(this.getAssets(), "font/RobotoThin.tf");

    }
}
