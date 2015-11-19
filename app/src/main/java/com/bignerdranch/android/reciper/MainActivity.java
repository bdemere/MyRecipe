package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        UUID a = new UUID(234,342);
        Intent i = SnapPagerActivity.newIntent(MainActivity.this, a);
        startActivity(i);
    }
}
