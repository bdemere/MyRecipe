package com.bignerdranch.android.reciper.SnapControllers;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.reciper.Comment.CommentDialog;
import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.PictureUtils;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/13/15.
 */
public class  NewSnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    final static String IS_CAMERA = "com.genius.android.reciper.IS_CAMERA";
    final static String COMMENT_DIALOG = "com.genius.android.reciper.COMMENT_DIALOG";

    private static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_COMMENTS = 1;


    private ImageView mSnapImage;
    private Button mRetakeButton;
    private Button mWrapUpButton;
    private ImageButton mAddSnapButton;
    private Bitmap mBackground;
    private Vibrator mVibrate;


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

    private Snap mCurrentSnap;
    private ArrayList<Snap> mSnaps;
    private RecipeBook mTheBook;
    private int snapID;
    private UUID recipeID;
    private Recipe mRecipe;
    private boolean isCamera;
    private File mPhotoFile;

    public float x;
    public float y;

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

        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapID);
        //mCurrentSnap.setComments(mTheBook.getComments(mCurrentSnap.getId()));

        mPhotoFile = mTheBook.getPhotoFile(mCurrentSnap);

        Log.d("TAG", "mPhotoFile set to snap with id: " + mCurrentSnap.getId());
        Log.d("TAG", "File name is: " + mCurrentSnap.getPictureFileName());
        Log.d("TAG", "onCreate called!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView() called");
        View v = inflater.inflate(R.layout.new_recipe_snap, container, false);


        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mAddSnapButton = (ImageButton) v.findViewById(R.id.add_snap_button);
        mSnapImage.setClickable(true);

        Log.d("TAG", "snap being  created with id: " + mCurrentSnap.getId() + " in recipe with ID: " + recipeID + " and isCamera: " + isCamera);
        mBackground = null;
        if(!isCamera) {
            mBackground = PictureUtils.getScaledBitmap(
                    RecipeBook.getTheRecipeBook(getActivity())
                            .getPhotoFile(mCurrentSnap).getPath(), getActivity());
        }

        //mBackground = BitmapFactory.decodeResource(getResources(), mCurrentSnap.getPicture());
        //mSnapImage.setScaleType(ImageView.ScaleType.MATRIX);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if(!isCamera) {
            //Drawable drawable = new BitmapDrawable(getResources(), mBackground);
            //Bitmap tempBackground = getResizedBitmap(RotateBitmap(mBackground, 90), width, height);
            //mBackground = tempBackground;
            //mSnapImage.setImageBitmap(tempBackground);
            mSnapImage.setImageBitmap(mBackground);
            //mSnapImage.setScaleType(ImageView.ScaleType.MATRIX);

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

                Toast.makeText(getActivity(), "SNAP ID: " + mCurrentSnap.getId(), Toast.LENGTH_LONG).show();
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
                mVibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrate.vibrate(25);
                String toastString = "x : " + x + " y: " + y;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                Log.d("fragment", "you long-touched at x: " + x + " y: " + y);
                Comment result = mCurrentSnap.searchComments(x,y);
                if(result == null) {
                    Bitmap tick = BitmapFactory.decodeResource(getResources(), R.drawable.commentn);
                    mSnapImage.setImageBitmap(overlayBitmapToxy(mBackground, getResizedBitmap(tick, 100, 100), x, y));
                }
                CommentDialog dialog = CommentDialog.newInstance(x, y, snapID, recipeID);
                dialog.setTargetFragment(NewSnapFragment.this, REQUEST_COMMENTS);
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

        if(resultCode != REQUEST_COMMENTS) {
            Log.d("TAG", "onActivityResult: REQUEST_COMMENTS");
            mSnaps = mTheBook.getSnaps(recipeID);
            mCurrentSnap = mSnaps.get(snapID);
        }

        if(requestCode == REQUEST_PHOTO) {
            updatePhotoView();
            Log.d("TAG", "onActivityResult: Request_Photo");
        }
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
        mBackground = overlayBitmap;
        return overlayBitmap;
    }
    public float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void updatePhotoView() {
        //RecipeBook.getTheRecipeBook(getActivity()).getLatestRecipe().newSnap();
        Snap snap = Recipe.newSnap(recipeID); //add a dummy snap
        mTheBook.addSnap(snap);

        Log.d("TAG", "newSnap created with id: " + snap.getId() + " in recipe with ID: "+ recipeID);
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