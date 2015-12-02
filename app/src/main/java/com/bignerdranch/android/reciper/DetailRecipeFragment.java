package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bignerdranch.android.reciper.SnapControllers.SnapPagerActivity;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by remember on 11/15/2015.
 */
public class DetailRecipeFragment extends Fragment {
    private static final String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    private PhotoAdapter mAdapter;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private UUID mRecipeID;
    private RecyclerView mPhotoRecyclerView;

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
        //mSnaps = mRecipe.getSnaps();
        mSnaps = RecipeBook.getTheRecipeBook(getActivity()).getSnaps(mRecipeID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_detail_page, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.snap_recycler_view);

        LinearLayoutManager layoutManager;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }

        mPhotoRecyclerView.setLayoutManager(layoutManager);
        updateUI();
        return v;
    }

    /*private void setupAdapter() {
        mPhotoRecyclerView.setAdapter(new PhotoAdapter());

    }*/

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.text);
        }

        public void bindDrawable(final int Position) {
            File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mSnaps.get(mSnaps.size() - 1 - Position));
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mItemImageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                //Bitmap tempBitmap = getResizedBitmap(RotateBitmap(bitmap, 90), 600, 1000);
                //bitmap = tempBitmap;
                mItemImageView.setImageBitmap(bitmap);
            }

            //mItemImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //mItemImageView.setImageBitmap(placeholder);

            mItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SnapPagerActivity.newIntent(getActivity(), mRecipeID, Position);
                    startActivity(intent);
                }
            });
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<Snap> snaps;

        public PhotoAdapter(ArrayList<Snap> snaps) {
            this.snaps = snaps;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            //mSnaps = RecipeBook.getTheRecipeBook().getRecipe(mRecipeID).getSnaps();
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

    private void updateUI(){
        mAdapter = new PhotoAdapter(mSnaps);
        mPhotoRecyclerView.setAdapter(mAdapter);
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
}
