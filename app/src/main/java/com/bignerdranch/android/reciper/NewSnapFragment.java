package com.bignerdranch.android.reciper;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bubujay on 11/13/15.
 */
public class NewSnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    final static String IS_CAMERA = "com.genius.android.reciper.IS_CAMERA";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    //private List<Snap> mRecipe;
    private Snap mCurrentSnap;
    private Button mVP;
    private TextView mcoordView;
    private TextView mTitle;
    private ImageView mSnapImage;
    private Bitmap mSnapBitmap;
    private Button mButtonTemp;
    /* ...on touch variables ... */
    private long mTouchStartTime;
    private long mLongClickDuration = 500;

    private boolean isShifted = false;
    //private int shiftFactor = 1;
    private Button mRetakeButton;
    private Button mWrapUpButton;
    private int snapID;
    private UUID recipeID;
    private boolean isCamera;
    public float x;
    public float y;
    private Button mAddSnapButton;

    /***camera page ***/
    private ImageButton mCloseButton;
    private ImageButton mCameraButton;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private File pictureFile;


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            pictureFile = new File(getContext().getFilesDir(), "someimagename.bmp");
            if (pictureFile == null){
                Log.d("YO", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Bitmap bit = BitmapFactory.decodeFile(pictureFile.getPath());
                Log.d("YO", "Bitmap file: " +bit);
            } catch (FileNotFoundException e) {
                Log.d("YO", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("YO", "Error accessing file: " + e.getMessage());
            }
        }
    };

    //private Bitmap background = ((BitmapDrawable)getResources().getDrawable(R.drawable.kitchen2)).getBitmap();

    public static NewSnapFragment newInstance(int position, UUID recipeID, boolean isCamera) {
        Bundle args = new Bundle();
        args.putSerializable(SNAP_ID, position);
        args.putSerializable(RECIPE_ID, recipeID);
        args.putSerializable(IS_CAMERA, isCamera);

        NewSnapFragment fragment = new NewSnapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapID = (int)getArguments().getSerializable(SNAP_ID);
        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        isCamera = (boolean)getArguments().getSerializable(IS_CAMERA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_recipe_snap, container, false);
        mCurrentSnap = RecipeBook.getTheRecipeBook().getRecipe(recipeID).getSnaps().get(snapID);

        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mAddSnapButton = (Button) v.findViewById(R.id.add_snap_button);
        mCloseButton = (ImageButton) v.findViewById(R.id.imgClose);
        mCameraButton = (ImageButton) v.findViewById(R.id.imageCapture);

        mSnapImage.setClickable(true);

        Bitmap background = BitmapFactory.decodeResource(getResources(), mCurrentSnap.getPicture());
        mSnapImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mSnapImage.setImageBitmap(background);

        if(isCamera) {
            mSnapImage.setVisibility(View.INVISIBLE);
            mRetakeButton.setVisibility(View.INVISIBLE);
            mWrapUpButton.setVisibility(View.INVISIBLE);
            mCloseButton.setVisibility(View.VISIBLE);
            mCameraButton.setVisibility(View.VISIBLE);

            try{
                mCamera = Camera.open();//you can use open(int) to use different cameras
            } catch (Exception e){
                Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            }

            mCameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCamera.takePicture(null, null, mPicture);
                    //Bitmap bit = BitmapFactory.decodeFile(pictureFile.getPath());
                    RecipeBook.getTheRecipeBook().getLatest().newSnap();
                    NewRecipeSnapPagerActivity.update();
                }
            });


            if(mCameraButton != null) {
                mCameraView = new CameraView(getActivity(), mCamera);
                FrameLayout frameCamera = (FrameLayout) v.findViewById(R.id.camera_view);
                frameCamera.addView(mCameraView);
            }

            //btn to close the application
            ImageButton imgClose = (ImageButton)v.findViewById(R.id.imgClose);
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(0);
                }
            });
        }else{
            mAddSnapButton.setVisibility(View.INVISIBLE);
            mCloseButton.setVisibility(View.INVISIBLE);
            mCameraButton.setVisibility(View.INVISIBLE);
        }

        mAddSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeBook.getTheRecipeBook().getLatest().newSnap();
                NewRecipeSnapPagerActivity.update();

            }
        });
        final CountDownTimer hideTimer = new CountDownTimer(1700,1000){
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

        mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mSnapImage", ": short Pressed");
                //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
                //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
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
        });

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
                String toastString = "x : " + x + " y: " + y;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                Log.d("fragment", "you long-touched at x: " + x + " y: " + y);
                CommentDialog dialog = CommentDialog.newInstance(x, y);
                dialog.show(getFragmentManager(), "comment at xy");
                return false;
            }
        });
        return v;
    }


    public float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

}
