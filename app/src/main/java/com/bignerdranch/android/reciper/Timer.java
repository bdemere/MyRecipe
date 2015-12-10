package com.bignerdranch.android.reciper;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by bimana2 on 12/3/15.
 */

public class Timer{
    private long startTime;
    private long endTime;
    private static Timer mTimer;
    private static Context mContext;

    private Timer(Context context){
        mContext = context.getApplicationContext();
    }

    public static Timer getTimer(Context context) {
        if (mTimer == null) {
            mTimer = new Timer(context);
        }
        return mTimer;
    }

    public long durationMinutes(){
        return TimeUnit.MILLISECONDS.toSeconds((endTime-startTime))/60;
    }

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public void stop(){
        endTime = System.currentTimeMillis();
    }

    public void reset(){
        startTime = 0;
        endTime = 0;
    }

}