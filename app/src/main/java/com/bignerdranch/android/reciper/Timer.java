package com.bignerdranch.android.reciper;

import android.content.Context;

import java.util.concurrent.TimeUnit;
/**
 *  A singleton timer class
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 12/3/15.
 */

public class Timer{
    private long startTime;
    private long endTime;
    private static Timer mTimer;
    private static Context mContext;

    private Timer(Context context){
        mContext = context.getApplicationContext();
    }

    /*
        * a method to return a timer object already present (or a new one)
        * if it does not exist
        * @param    context     the context
        * @return   a Time object
     */
    public static Timer getTimer(Context context) {
        if (mTimer == null) {
            mTimer = new Timer(context);
        }
        return mTimer;
    }

    /*
        * a method to return elapsed time
        * @return   recorded time
     */
    public long durationMinutes(){
        return TimeUnit.MILLISECONDS.toSeconds((endTime-startTime))/60;
    }

    /*
        * start timer
     */
    public void start(){
        startTime = System.currentTimeMillis();
    }
    /*
        * stop timer
     */
    public void stop(){
        endTime = System.currentTimeMillis();
    }

    /*
        * reset timer
     */
    public void reset(){
        startTime = 0;
        endTime = 0;
    }

}