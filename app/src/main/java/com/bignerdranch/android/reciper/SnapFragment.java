package com.bignerdranch.android.reciper;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by bubujay on 11/13/15.
 */
public class SnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    //private List<Snap> mRecipe;
    private GestureListener mGestureListener;
    private Snap mCurrentSnap;
    private Button mVP;
    private TextView mcoordView;
    private TextView mTitle;
    private ImageView mSnapImage;
    private Bitmap mSnapBitmap;
    private Button mButtonTemp;
    /* ...on touch variables ... */
    private long mDuration;
    private long mLongClickDuration = 500;

    private int isShifted = 1;
    //private int shiftFactor = 1;
    private Button mRetakeButton;
    private Button mWrapupButton;
    private int snapID;
    private UUID recipeID;
    public float x;
    public float y;

    //private Bitmap background = ((BitmapDrawable)getResources().getDrawable(R.drawable.kitchen2)).getBitmap();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_snap, container, false);
        mCurrentSnap = RecipeBook.getTheRecipeBook().getRecipe(recipeID).getSnaps().get(snapID);

        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapupButton = (Button) v.findViewById(R.id.wrapup_button);
        mSnapImage.setClickable(true);
        mSnapImage.setLongClickable(true);

        Bitmap background = BitmapFactory.decodeResource(getResources(), mCurrentSnap.getPicture());
        mSnapImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mSnapImage.setImageBitmap(background);

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
        //mSnapImage.setTranslationY(isShifted * mRetakeButton.getHeight());
        mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSnapImage.setTranslationY(isShifted * mRetakeButton.getHeight());//.setDuration(300);
                //mRetakeButton.setVisibility(View.VISIBLE);
                //mWrapupButton.setVisibility(View.VISIBLE);


                Log.d("isittrue", "" + isShifted);
            }
        });

        mSnapImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getRawX();
                    y = event.getRawY();
                    String toastString = "x : " + x + " y: " + y;
                    //Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                    Log.d("fragment", "you touched at x: " + x + " y: " + y);
                    //CommentDialog dialog = CommentDialog.newInstance(x, y);
                    //dialog.show(getFragmentManager(), "comment at xy");//

                }*/
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDuration = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - mDuration) > mLongClickDuration) {
                        //if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            x = event.getRawX();
                            y = event.getRawY();
                            String toastString = "x : " + x + " y: " + y;
                            //Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                            Log.d("fragment", "you touched at x: " + x + " y: " + y);
                            CommentDialog dialog = CommentDialog.newInstance(x, y);
                            dialog.show(getFragmentManager(), "comment at xy");//

                       // }
                        Log.d("mSnapImage", ": long pressed");
                        return false;
                    } else {
                        Log.d("mSnapImage", ": short Pressed");
                        //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
                        //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
                        if(isShifted == 1) {
                            mRetakeButton.setVisibility(View.VISIBLE);
                            mWrapupButton.setVisibility(View.VISIBLE);
                            //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
                        }
                        else {
                            mRetakeButton.setVisibility(View.GONE);
                            mWrapupButton.setVisibility(View.GONE);
                            //mSnapImage.animate().y(isShifted * mRetakeButton.getHeight()).setDuration(300);
                        }

                        isShifted = -1 * isShifted;
                        System.out.println("Short Click has happened...");
                        return true;
                    }
                }
                return true;
            }
        });

        mSnapImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(),"long clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mRetakeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "wat up", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return v;
    }

    private class GestureListener implements GestureDetector.OnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

}
