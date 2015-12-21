package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.reciper.SnapControllers.SnapPagerActivity;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;

import java.io.File;
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

    // member variables
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private UUID mRecipeID;

    private RecyclerView mPhotoRecyclerView;
    private ImageView mRecipeProfile;
    private PhotoAdapter mAdapter;
    private TextView mTitle;
    private TextView mDate;
    private TextView mCategory;
    private TextView mDuration;
    private TextView mLevel;
    private TextView mServings;
    private TextView mTags;

    /**
     * Creates a new instance of this fragment
     */
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

        // initialize member variables
        mRecipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mRecipe = RecipeBook.getTheRecipeBook(getActivity()).getRecipe(mRecipeID);
        mSnaps = RecipeBook.getTheRecipeBook(getActivity()).getSnaps(mRecipeID);

        // this page has two menu options
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
                // delete the current recipe
                RecipeBook.getTheRecipeBook(getActivity()).deleteRecipes(mRecipe);
                 intent = new Intent(getActivity(), RecipeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                // destroy current activity
                getActivity().finish();
                return true;
            case R.id.menu_item_edit_recipe:
                // open page for editing recipe info
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

        // get reference to UI views
        mTitle = (TextView) v.findViewById(R.id.title_text_view);
        mDate = (TextView) v.findViewById(R.id.date_text_view);
        mCategory = (TextView) v.findViewById(R.id.category_text_view);
        mDuration = (TextView) v.findViewById(R.id.duration_text_view);
        mLevel = (TextView) v.findViewById(R.id.level_text_view);
        mServings = (TextView) v.findViewById(R.id.servings_text_view);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.snap_recycler_view);
        mRecipeProfile = (ImageView) v.findViewById(R.id.recipe_profile_image);
        mTags = (TextView) v.findViewById(R.id.tags_text_view);

        // determine the orientation of the layout
        LinearLayoutManager layoutManager;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }

        // create instance for formatting date recipe was created
        DateFormat f = SimpleDateFormat.getDateInstance();

        // set UI view content to data related to current recipe
        mTitle.setText(mRecipe.getTitle());
        mDate.setText(f.format(mRecipe.getDate()));
        mCategory.setText(mRecipe.getCategory());
        mDuration.setText(mRecipe.getDuration() + " min");
        mLevel.setText(mRecipe.getDifficulty());
        mServings.setText(mRecipe.getServings());
        mTags.setText("TAGS: " + mRecipe.getTags());
        mPhotoRecyclerView.setLayoutManager(layoutManager);

        // load image in the gallery
        if(mSnaps.size() > 1) {
            File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mSnaps.get(1));
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
            mRecipeProfile.setImageBitmap(PictureUtils.RotateBitmap(bitmap, 90));
        }

        // attach adapter
        mAdapter = new PhotoAdapter(mSnaps);
        mPhotoRecyclerView.setAdapter(mAdapter);

        return v;
    }


    /**
     *   ViewHolder for displaying snaps in a RecyclerView
     */
    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.text);
        }

        /**
         * Binds an image associated with a snap to the ImageView
         *
         * @param    Position    position of the snap in the recipe
         */
        public void bindDrawable(final int Position) {
            // get image file
            File mPhotoFile = RecipeBook.getTheRecipeBook(
                    getActivity()).getPhotoFile(mSnaps.get(mSnaps.size() - 1 - Position));

            // display image
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mItemImageView.setImageDrawable(null);
            } else {
                // resize and rotate the bitmap so that it optimally fits in the gallery
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
                Bitmap tempBitmap = PictureUtils.
                        getResizedBitmap(PictureUtils.RotateBitmap(bitmap, 90), 600, 1000);
                bitmap = tempBitmap;
                mItemImageView.setImageBitmap(bitmap);
            }

            // onclick listener that opens ViewPager acticity when clicked on an image
            mItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SnapPagerActivity.newIntent(getActivity(), mRecipeID, Position);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     *   Adapter for displaying snaps in a RecyclerView
     */
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<Snap> snaps;

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
}
