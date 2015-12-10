package com.bignerdranch.android.reciper.Comment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bubujay on 11/18/15.
 */
public class VideoCommentDialog extends DialogFragment implements SurfaceHolder.Callback{
    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.CommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private boolean mInitSuccesful;
    private boolean surfaceClicked = false;
    private ProgressBar mVineProgressBar;
    private  Thread mVineThread;
    private int progress;
    private Thread sec_6;
    private Handler handler = new Handler();


    private EditText mComment;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private Button mAddButton;
    private Snap mCurrentSnap;
    public VideoCommentDialog(){
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "MyCameraApp");
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
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_"+ timeStamp + ".mp4");


        return mediaFile;
    }
    public static VideoCommentDialog newInstance(float positionX, float positionY, int snapPosition, UUID recipeId){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        VideoCommentDialog fragment = new VideoCommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mX = (float)getArguments().getSerializable(POSITION_X);
        mY = (float)getArguments().getSerializable(POSITION_Y);
        snapPos = (int)getArguments().getSerializable(SNAP_POSITION);

        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapPos);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);

    }
    /* Init the MediaRecorder, the order the methods are called is vital to
 * its correct functioning */
    private void initRecorder(Surface surface) throws IOException {
        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.unlock();
        }

        if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setOutputFile(getOutputMediaFile().getPath());


        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();

        // once the objects have been released they can't be reused
        mMediaRecorder = null;
        mCamera = null;
    }


    @Override
    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = 3/4 * size.x;
        int height = size.y;
        window.setLayout(1080, 720);
        window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        sendResult(Activity.RESULT_OK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_comment_dialog_fragment, container);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // set "origin" to top left corner
        //getDialog().setTitle("Comment");
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = Math.round(mY);
        window.setAttributes(params);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        mVineProgressBar = (ProgressBar) view.findViewById(R.id.vine_progressBar);

        mVineThread = new Thread(new Runnable(){
            public void run(){
                while (progress < 100){
                    progress += 1;
                    handler.post(new Runnable() {
                        public void run(){
                            mVineProgressBar.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sec_6 = new Thread(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(6 * 1000 + 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(surfaceClicked){
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                }
                getDialog().dismiss();
            }
        });


        //mVineThread.start();

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        /*mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(View v) {
                if (!surfaceClicked) {
                    mMediaRecorder.start();
                    try {
                        Thread.sleep(6 * 1000); // This will recode for 10 seconds, if you don't want then just remove it.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    surfaceClicked = !surfaceClicked;
                    getDialog().dismiss();
                }

                else {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    try {
                        initRecorder(mHolder.getSurface());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    surfaceClicked = !surfaceClicked;
                }
            }
        });*/



        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surfaceClicked == false) {
                    surfaceClicked = !surfaceClicked;
                    mMediaRecorder.start();
                    mVineThread.start();
                    sec_6.start();

                    //getDialog().dismiss();
                }else if(surfaceClicked == true){
                    surfaceClicked = !surfaceClicked;
                    mVineThread.interrupt();
                    sec_6.interrupt();
                    progress = 0;
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    try {
                        initRecorder(mHolder.getSurface());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return view;
    }


    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}