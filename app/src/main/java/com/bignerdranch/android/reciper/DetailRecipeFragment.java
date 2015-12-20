package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.SnapControllers.SnapPagerActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 *  A fragment class to control the detail page of a recipe
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/15/2015.
 */
public class DetailRecipeFragment extends Fragment {
    private static final String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    private PhotoAdapter mAdapter;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private UUID mRecipeID;
    private RecyclerView mPhotoRecyclerView;
    private ImageView mRecipeProfile;

    private TextView mTitle;
    private TextView mDate;
    private TextView mCategory;
    private TextView mDuration;
    private TextView mLevel;
    private TextView mServings;

    public static DetailRecipeFragment newInstance(UUID RecipeID) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ID, RecipeID);
        DetailRecipeFragment fragment = new DetailRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mRecipe = RecipeBook.getTheRecipeBook(getActivity()).getRecipe(mRecipeID);
        mSnaps = RecipeBook.getTheRecipeBook(getActivity()).getSnaps(mRecipeID);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_item_delete_recipe:
                RecipeBook.getTheRecipeBook(getActivity()).deleteRecipes(mRecipe);
                 intent = new Intent(getActivity(), RecipeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                return true;
            case R.id.menu_item_edit_recipe:
                intent = RecipeInfoFormActivity.newIntent(getActivity(), mRecipe.getID(), false);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_detail_page, container, false);

        //get reference to UI views
        mTitle = (TextView) v.findViewById(R.id.title_text_view);
        mDate = (TextView) v.findViewById(R.id.date_text_view);
        mCategory = (TextView) v.findViewById(R.id.category_text_view);
        mDuration = (TextView) v.findViewById(R.id.duration_text_view);
        mLevel = (TextView) v.findViewById(R.id.level_text_view);
        mServings = (TextView) v.findViewById(R.id.servings_text_view);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.snap_recycler_view);
        mRecipeProfile = (ImageView) v.findViewById(R.id.recipe_profile_image);

        //manage the orientation of the layout
        LinearLayoutManager layoutManager;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }

        DateFormat f = SimpleDateFormat.getDateInstance();

        //set UI views to data related to current recipe
        mTitle.setText(mRecipe.getTitle());
        mDate.setText(f.format(mRecipe.getDate()));
        mCategory.setText(mRecipe.getCategory());
        mDuration.setText(mRecipe.getDuration() + " min");
        mLevel.setText(mRecipe.getDifficulty());
        mServings.setText(mRecipe.getServings());

        mPhotoRecyclerView.setLayoutManager(layoutManager);

        //load image in the gallery
        if(mSnaps.size() > 1) {
            File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mSnaps.get(1));
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
            mRecipeProfile.setImageBitmap(RotateBitmap(bitmap,90));
        }
        updateUI();
        return v;
    }


    /*
        A view holder inner class
     */
    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;

        /* constructor */
        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.text);
        }

        /*
            * Binds an image associated with a snap to the ImageView
            * @param    Position    position of the snap in the recipe
         */
        public void bindDrawable(final int Position) {
            File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mSnaps.get(mSnaps.size() - 1 - Position));
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mItemImageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
                //resize and rotates the bitmap so that it optimally fits in the gallery
                Bitmap tempBitmap = getResizedBitmap(RotateBitmap(bitmap, 90), 600, 1000);
                bitmap = tempBitmap;
                mItemImageView.setImageBitmap(bitmap);
            }

            /*
                * an onclick listener that responds to clicking on an image in the gallery
                * by opening the image itself in a pager activity
             */
            mItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SnapPagerActivity.newIntent(getActivity(), mRecipeID, Position);
                    startActivity(intent);
                }
            });
        }
    }

    /*
        An adapter inner class
     */
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<Snap> snaps;

        /*
            * A constructor that initializes ArrayList of snaps
        */
        public PhotoAdapter(ArrayList<Snap> snaps) {
            this.snaps = snaps;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            photoHolder.bindDrawable(position);
        }

        @Override
        public int getItemCount() {
            return snaps.size();
        }
    }

    /*
        Updates the recycler view with an adapter
     */
    private void updateUI(){
        mAdapter = new PhotoAdapter(mSnaps);
        mPhotoRecyclerView.setAdapter(mAdapter);
    }

    /*
        * Source: http://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
        * A method to rotate a bitmap
        * @param    source      bitmap to resize
        * @param    angle       angel to rotate with
        * @return               rotated bitmap
    */

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /*
        * Source: http://stackoverflow.com/questions/8133029/how-to-know-bitmap-size-after-reducing-its-size-in-android
        * A method to resize Bitmap
        * @param    bm          bitmap to resize
        * @param    newWidth    new width of the output bitmap
        * @param    newHeight   new Height of the output bitmap
        * @return               resized bitmap
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap
                (bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
