package com.bignerdranch.android.reciper.SnapControllers;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bignerdranch.android.reciper.CommentDialogs.CommentDialog;
import com.bignerdranch.android.reciper.CommentDialogs.EditCommentDialog;
import com.bignerdranch.android.reciper.HomePageActivity;
import com.bignerdranch.android.reciper.PictureUtils;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.RecipeInfoFormActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 *  Fragment for displaying snaps of a recipe being created
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/13/2015.
 */
public class NewSnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    final static String IS_CAMERA = "com.genius.android.reciper.IS_CAMERA";

    private static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_COMMENTS = 1;

    // member variables
    private ImageView mSnapImage;
    private Button mRetakeButton;
    private Button mWrapUpButton;
    private ImageButton mAddSnapButton;
    private Bitmap mBackground;
    private Vibrator mVibrate;
    private LinearLayout mFinishCancelLayout;
    private Button mFinishButton;
    private Button mCancelButton;
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

    /**
     * Creates a new instance of this fragment
     */
    public static NewSnapFragment newInstance(int position, UUID recipeID, boolean isCamera) {
        // build necessary arguments to create this fragment
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
        // initialize member variables
        snapID = (int)getArguments().getSerializable(SNAP_ID);
        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        isCamera = (boolean)getArguments().getSerializable(IS_CAMERA);
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapID);
        mPhotoFile = mTheBook.getPhotoFile(mCurrentSnap);
    }

    @Override
    public void onResume() {
        super.onResume();
        // load snaps from database and draw comment locations on resume
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapID);
        mSnapImage.setImageBitmap(drawCommentLocations(mBackground));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_recipe_snap, container, false);

        // get reference to UI views
        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mAddSnapButton = (ImageButton) v.findViewById(R.id.add_snap_button);
        mFinishCancelLayout = (LinearLayout) v.findViewById(R.id.finish_cancel_layout);
        mFinishButton = (Button) v.findViewById(R.id.finish_button);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mSnapImage.setClickable(true);

        mBackground = null;
        if(!isCamera) {
            mBackground = BitmapFactory.decodeFile(RecipeBook.getTheRecipeBook(getActivity())
                    .getPhotoFile(mCurrentSnap).getPath());
        }

        // get window size for resizing image
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // if not isCamera set background to be the snap image
        if(!isCamera) {
            Bitmap tempBackground = PictureUtils.getResizedBitmap(
                    PictureUtils.RotateBitmap(mBackground, 90), width, height);
            mBackground = tempBackground;
            mSnapImage.setImageBitmap(tempBackground);
        }

        // set which UI elements should be visible based on isCamera boolean
        if(isCamera) {
            mSnapImage.setVisibility(View.INVISIBLE);
            mRetakeButton.setVisibility(View.INVISIBLE);
            mWrapUpButton.setVisibility(View.INVISIBLE);

        }else{
            mAddSnapButton.setVisibility(View.INVISIBLE);
            mFinishCancelLayout.setVisibility(View.INVISIBLE);
        }


        // delete new reciepe and exit
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if not snap added just quit
                if(mSnaps.size() == 1) {
                    mTheBook.deleteRecipes(mRecipe);
                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    // if snaps added, show AlertDialog to make sure the user wants to delete
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Your recipe will not be saved. Are you sure? ")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if yes, delete recipe and destory current activity
                                    mTheBook.deleteRecipes(mRecipe);
                                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }


            }
        });

        // finish recipe and open info page
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if any pictures have been added
                if(mSnaps.size() == 1) {
                    Toast.makeText(getActivity(), "No pictures added", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = RecipeInfoFormActivity.newIntent(getActivity(), mRecipe.getID(),true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }

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

        // open a comment dialog
        mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrate.vibrate(25);
                Comment result = mCurrentSnap.searchComments(x,y);
                if(result == null) {
                    // if no previous comments exists at this xy location open new comment dialog
                    CommentDialog dialog = CommentDialog.newInstance(x, y, snapID, recipeID);
                    dialog.setTargetFragment(NewSnapFragment.this, REQUEST_COMMENTS);
                    dialog.show(getFragmentManager(), "comment at xy");
                }else {
                    // if there is previous one, open edit comment dialog
                    EditCommentDialog dialog = EditCommentDialog.newInstance(x, y, snapID, recipeID);
                    dialog.setTargetFragment(NewSnapFragment.this, REQUEST_COMMENTS);
                    dialog.show(getFragmentManager(), "comment at xy");
                }
            }
        });

        // check if phone can capture image
        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        if(canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // open camera
        mAddSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        // new comment added
        if(requestCode == REQUEST_COMMENTS) {
            mSnaps = mTheBook.getSnaps(recipeID);
            mCurrentSnap = mSnaps.get(snapID);
            mSnapImage.setImageBitmap(drawCommentLocations(mBackground));
        }

        // new photo captured
        if(requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    /**
     * Draw all comments onto a bitmap
     * @param bitmap
     * @return new bitmap with comments drawn
     */
    public Bitmap drawCommentLocations(Bitmap bitmap){
        Bitmap tick = BitmapFactory.decodeResource(getResources(), R.drawable.commentn);
        Bitmap smallTick = PictureUtils.getResizedBitmap(tick, 100, 100);

        ArrayList<Comment> currentSnapComments = mCurrentSnap.getComments();

        for(Comment comment:currentSnapComments){
            Bitmap tempBitmap = PictureUtils.overlayBitmapToxy(
                    bitmap, smallTick, comment.getX(), comment.getY());
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    /**
     * Addes new snap to database and notifies the parent activity that dataset has changed
     */
    private void updatePhotoView() {
        Snap snap = Recipe.newSnap(recipeID); //add a dummy snap
        mTheBook.addSnap(snap);
        ((NewRecipeSnapPagerActivity) getActivity()).update();

        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mSnapImage.setImageDrawable(null);
        }
    }
}