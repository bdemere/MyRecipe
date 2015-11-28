package com.bignerdranch.android.reciper;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.reciper.data.Recipe;
import com.bignerdranch.android.reciper.data.Snap;

import java.io.File;
import java.util.UUID;

/**
 * Created by bubujay on 11/13/15.
 */
public class NewSnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    final static String IS_CAMERA = "com.genius.android.reciper.IS_CAMERA";

    private static final int REQUEST_PHOTO = 0;

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
    private Recipe mRecipe;
    private boolean isCamera;
    public float x;
    public float y;
    private Button mAddSnapButton;

    private File mPhotoFile;

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
        mRecipe = RecipeBook.getTheRecipeBook(getActivity()).getRecipe(recipeID);
        mCurrentSnap = mRecipe.getSnap(snapID);
        mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mCurrentSnap);

        Log.d("TAG", "mPhotoFile set to snap with id: " + mCurrentSnap.getID());
        Log.d("TAG", "File name is: " + mCurrentSnap.getPictureFileName());
        Log.d("TAG", "onCreate called!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_recipe_snap, container, false);


        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mAddSnapButton = (Button) v.findViewById(R.id.add_snap_button);
        mSnapImage.setClickable(true);

        Log.d("TAG", "snap being  created with id: " + mCurrentSnap.getID() + " in recipe with ID: " + recipeID + " and isCamera: " + isCamera);
        Bitmap bitmap = null;
        if(!isCamera) {
            bitmap = PictureUtils.getScaledBitmap(
                    RecipeBook.getTheRecipeBook(getActivity())
                            .getPhotoFile(mCurrentSnap).getPath(), getActivity());
        }

        Bitmap background = BitmapFactory.decodeResource(getResources(), mCurrentSnap.getPicture());
        mSnapImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        if(!isCamera) {
            mSnapImage.setImageBitmap(bitmap);
        }

        if(isCamera) {
            mSnapImage.setVisibility(View.INVISIBLE);
            mRetakeButton.setVisibility(View.INVISIBLE);
            mWrapUpButton.setVisibility(View.INVISIBLE);
        }else{
            mAddSnapButton.setVisibility(View.INVISIBLE);
        }

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

                Toast.makeText(getActivity(), "SNAP ID: " + mCurrentSnap.getID(), Toast.LENGTH_LONG).show();
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


        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        if(canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mAddSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        //updatePhotoView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_PHOTO) {
            updatePhotoView();
            Log.d("TAG", "onActivityResult: Request_Photo");
        }
    }

    public float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void updatePhotoView() {
        //RecipeBook.getTheRecipeBook(getActivity()).getLatest().newSnap();
        Snap newSnap = RecipeBook.getTheRecipeBook(getActivity()).getRecipe(recipeID).newSnap();
        Log.d("TAG", "newSnap created with id: " + newSnap.getID() + " in recipe with ID: "+ recipeID);
        ((NewRecipeSnapPagerActivity) getActivity()).update();


        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mSnapImage.setImageDrawable(null);
        } else {
            Log.d("TAG", "Updateed image!!!!!!!!!!!");
            //Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());

            //Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            /*Bitmap bitmap = PictureUtils.getScaledBitmap(
                    RecipeBook.getTheRecipeBook(getActivity())
                            .getPhotoFile(newSnap).getPath(), getActivity());*/
            //mSnapImage.setImageBitmap(bitmap);
        }
    }
}