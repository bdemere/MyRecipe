package com.bignerdranch.android.reciper.SnapControllers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.reciper.CommentDialogs.DisplayCommentsDialog;
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
 *  Fragment for displaying snaps of already saved recipe
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/13/2015.
 */
public class SnapFragment extends Fragment{

    final static String SNAP_ID = "com.genius.android.reciper.SNAP_ID";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    // member variables
    private ImageView mSnapImage;
    private Button mRetakeButton;
    private Button mWrapUpButton;
    private int snapID;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private Snap mCurrentSnap;
    public float x;
    public float y;

    /**
     * Creates a new instance of this fragment
     */
    public static SnapFragment newInstance(int position, UUID recipeID) {
        // build necessary arguments to create this fragment
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
        //initialize member variables
        snapID = (int)getArguments().getSerializable(SNAP_ID);
        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_snap, container, false);

        // get reference to UI views
        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mSnapImage.setClickable(true);

        // get photo file of current snap and create a bitmap
        File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mCurrentSnap);
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());

        // get windows sizes
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // resize bitmap and draw comment locations
        Bitmap tempBitmap = PictureUtils.getResizedBitmap(
                PictureUtils.RotateBitmap(bitmap, 90), width, height);
        bitmap = drawCommentLocations(tempBitmap);
        mSnapImage.setImageBitmap(bitmap);

        mSnapImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getRawX();
                    y = event.getRawY();
                }
                return false;
            }
        });

        mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment result = mCurrentSnap.searchComments(x, y);

                if (result == null)
                    Toast.makeText(getActivity(), "No Comment", Toast.LENGTH_SHORT).show();
                else {
                    DisplayCommentsDialog dialog = DisplayCommentsDialog.newInstance(x, y, recipeID, snapID);
                    dialog.show(getFragmentManager(), "comment at xy");
                }
            }
        });

        return v;
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
            Bitmap tempBitmap = PictureUtils.overlayBitmapToxy(bitmap, smallTick, comment.getX(), comment.getY());
            bitmap = tempBitmap;
        }
        return bitmap;
    }
}
