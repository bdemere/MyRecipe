package com.bignerdranch.android.reciper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 *  Picture utility functions
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 12/20/15.
 */
public class PictureUtils {

    /**
     * Overlays a bitmap on a background bitmap at give location
     * @param Background background bitmap
     * @param tick bitmap to be layed on top
     * @param mx x coordinate
     * @param my y coordinate
     * @return overlated bitmap
     */
    public static Bitmap overlayBitmapToxy(Bitmap Background, Bitmap tick, float mx, float my) {
        Bitmap backgroundCopy = Background.copy(Bitmap.Config.ARGB_8888, true);
        int backgroundWidth = backgroundCopy.getWidth();
        int backgroundHeight = backgroundCopy.getHeight();
        int tickWidth = tick.getWidth();
        int tickHeight = tick.getHeight();

        float marginLeft = (float)( mx - tickWidth * 0.5);
        float marginTop = (float)( my - tickHeight * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(
                backgroundWidth, backgroundHeight, backgroundCopy.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(backgroundCopy, new Matrix(), null);
        canvas.drawBitmap(tick, marginLeft, marginTop, null);
        //mBackground = overlayBitmap;
        return overlayBitmap;
    }

    /**
     * Resizes a bitmap to a specified hight and width
     * @source http://stackoverflow.com/questions/8133029/how-to-know-bitmap-size-after-reducing-its-size-in-android
     * @param bm bitmap
     * @param newWidth new width
     * @param newHeight new height
     * @return new bitmap with new size
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * Rotates a bitmap to a spcified angle
     * @source http://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
     * @param source source bitmap
     * @param angle angle of rotation
     * @return rotated bitmap
     */
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
