package com.bignerdranch.android.reciper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by remember on 11/2/2015.
 */
public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        return BitmapFactory.decodeFile(path);
    }
}
