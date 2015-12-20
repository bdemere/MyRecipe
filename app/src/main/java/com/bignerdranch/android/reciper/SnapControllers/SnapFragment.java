package com.bignerdranch.android.reciper.SnapControllers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.bignerdranch.android.reciper.Comment.DisplayCommentsDialog;
import com.bignerdranch.android.reciper.Models.Recipe;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_snap, container, false);

        mSnapImage = (ImageView) v.findViewById(R.id.snap_imageView);
        mRetakeButton = (Button) v.findViewById(R.id.retake_button);
        mWrapUpButton = (Button) v.findViewById(R.id.wrapup_button);
        mSnapImage.setClickable(true);

        File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mCurrentSnap);
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Bitmap tempBitmap = getResizedBitmap(RotateBitmap(bitmap, 90), width, height);
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

    public Bitmap drawCommentLocations(Bitmap bitmap){
        Bitmap tick = BitmapFactory.decodeResource(getResources(), R.drawable.commentn);
        Bitmap smallTick =  getResizedBitmap(tick, 100, 100);

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

        float marginLeft = (float)( mx - tickWidth * 0.5);
        float marginTop = (float)( my - tickHeight * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(backgroundWidth, backgroundHeight, backgroundCopy.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(backgroundCopy, new Matrix(), null);
        canvas.drawBitmap(tick, marginLeft, marginTop, null);
        return overlayBitmap;
    }
}
