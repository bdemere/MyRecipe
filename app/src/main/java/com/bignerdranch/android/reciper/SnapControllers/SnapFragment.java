package com.bignerdranch.android.reciper.SnapControllers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.reciper.Dialogs.DisplayCommentsDialog;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.PictureUtils;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/13/15.
 */
public class SnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    private ImageView mSnapImage;
    private Button mRetakeButton;
    private Button mWrapUpButton;

    private Button mVP;
    private TextView mcoordView;
    private TextView mTitle;
    private Bitmap mSnapBitmap;
    private Button mButtonTemp;
    /* ...on touch variables ... */
    private long mTouchStartTime;
    private long mLongClickDuration = 500;

    private boolean isShifted = false;
    //private int shiftFactor = 1;

    private int snapID;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private Snap mCurrentSnap;

    public float x;
    public float y;


    public static SnapFragment newInstance(int position, UUID recipeID) {
        Bundle args = new Bundle();
        args.putSerializable(SNAP_ID, position);
        args.putSerializable(RECIPE_ID, recipeID);

        SnapFragment fragment = new SnapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapID = (int)getArguments().getSerializable(SNAP_ID);
        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);

        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapID);
        //mCurrentSnap.setComments(mTheBook.getComments(mCurrentSnap.getId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_snap, container, false);

        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mSnapImage.setClickable(true);

        Bitmap background = BitmapFactory.decodeResource(getResources(), mCurrentSnap.getPicture());

        File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mCurrentSnap);
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //mSnapImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        //mSnapImage.setImageDrawable(drawable);
        Bitmap tempBitmap = getResizedBitmap(RotateBitmap(bitmap, 90), width, height);
        bitmap = drawCommentLocations(tempBitmap);
        //bitmap = drawCommentLocations(bitmap);
        mSnapImage.setImageBitmap(bitmap);
        //mSnapImage.setImageBitmap(bitmap);

        //mButtonTemp = (Button) v.findViewById(R.id.button_temp);

       /* Recipe.getThisRecipe().testImageSetter();
        mRecipe = Recipe.getThisRecipe().getSnaps();
        mSnapImage = (ImageView) v.findViewById(R.id.snap_image);
        mVP = (Button) v.findViewById(R.id.vpager_button);
        mSnapImage.setImageResource(mRecipe.get(snapID).getPicture());

        mVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Log.d("fragment", "you touched at x: " + x + " y: " + y);
                String toastString = "x : " + x + " y: " + y;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
            }
        });
        */
        //mSnapImage.animate().y(-1*(mRetakeButton.getHeight())).setDuration(400);

        //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(1000);
        final CountDownTimer hideTimer = new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if(isShifted){
                    float shiftFactorR = 250;
                    float shiftFactorW = 250;
                    int speed = 200;
                    mRetakeButton.animate().xBy(-shiftFactorR).setDuration(speed);
                    mWrapUpButton.animate().xBy(shiftFactorW).setDuration(speed);
                    isShifted = !isShifted;
                }
            }
        }.start();

        /*mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mSnapImage", ": short Pressed");
                float shiftFactorR = 250;//dpToPx(mRetakeButton.getWidth());
                float shiftFactorW = 250;//dpToPx(mWrapUpButton.getWidth());
                int speed = 200;
                if (!isShifted) {
                    mRetakeButton.animate().xBy(shiftFactorR).setDuration(speed);
                    mWrapUpButton.animate().xBy(-shiftFactorW).setDuration(speed);
                } else {
                    mRetakeButton.animate().xBy(-shiftFactorR).setDuration(speed);
                    mWrapUpButton.animate().xBy(shiftFactorW).setDuration(speed);
                }
                isShifted = !isShifted;
                hideTimer.start();
            }
        });*/

        mSnapImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getRawX();
                    y = event.getRawY();
                }
                return false;
            }
        });


        mSnapImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*String toastString = "x : " + x + " y: " + y;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                Log.d("fragment", "you long-touched at x: " + x + " y: " + y);
                CommentDialog dialog = CommentDialog.newInstance(x, y, snapID);
                dialog.show(getFragmentManager(), "comment at xy");*/
                //Comment result = RecipeBook.getTheRecipeBook(getContext()).getRecipe(recipeID).getSnap(snapID).getLatestComment();//mCurrentSnap.searchComments((int)x, (int)y);

                Comment result = mCurrentSnap.searchComments(x, y);

                if(result == null)
                    Toast.makeText(getActivity(), "No Comment", Toast.LENGTH_SHORT).show();
                else {
                    DisplayCommentsDialog dialog = DisplayCommentsDialog.newInstance(x, y, recipeID, snapID);
                    dialog.show(getFragmentManager(), "comment at xy");
                }
                return false;
            }
        });
        return v;
    }

    public Bitmap drawCommentLocations(Bitmap bitmap){
        Bitmap tick = BitmapFactory.decodeResource(getResources(), R.drawable.commentn);
        Bitmap smallTick =  getResizedBitmap(tick, 100, 100);

        //mCurrentSnap.setComments(mTheBook.getComments(mCurrentSnap.getId()));
        ArrayList<Comment> currentSnapComments = mCurrentSnap.getComments();

        for(Comment comment:currentSnapComments){
            Bitmap tempBitmap = overlayBitmapToxy(bitmap, smallTick, comment.getX(), comment.getY());
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
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

    public Bitmap overlayBitmapToxy(Bitmap Background, Bitmap tick, float mx, float my) {
        Bitmap backgroundCopy = Background.copy(Bitmap.Config.ARGB_8888, true);
        int backgroundWidth = backgroundCopy.getWidth();
        int backgroundHeight = backgroundCopy.getHeight();
        int tickWidth = tick.getWidth();
        int tickHeight = tick.getHeight();

        //float marginLeft = (float)(backgroundWidth  -mx);//tickWidth * 0.5);
        //float marginTop =  (float)(backgroundHeight - my);//tickHeight * 0.5);
        float marginLeft = (float)( mx - tickWidth * 0.5);
        float marginTop = (float)( my - tickHeight * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(backgroundWidth, backgroundHeight, backgroundCopy.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(backgroundCopy, new Matrix(), null);
        canvas.drawBitmap(tick, marginLeft, marginTop, null);
        return overlayBitmap;
    }
}
