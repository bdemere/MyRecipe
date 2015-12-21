package com.bignerdranch.android.reciper;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 *  Singleton class to record how long cooking process took
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 12/03/15.
 */

public class Timer{
    private long startTime;
    private long endTime;
    private static Timer mTimer;
    private static Context mContext;

    /**
     * Constructor of Timer object
     * @param context application context
     */
    private Timer(Context context){
        mContext = context.getApplicationContext();
    }

    /**
     * Get instance of timer
     * @param context application content
     * @return
     */
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