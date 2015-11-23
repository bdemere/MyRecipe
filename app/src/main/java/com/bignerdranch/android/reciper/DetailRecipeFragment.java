package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by remember on 11/15/2015.
 */
public class DetailRecipeFragment extends Fragment {
    private static final String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    private PhotoAdapter mAdapter;
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

        public void bindDrawable(int Picture, final int Position) {
            Bitmap placeholder = BitmapFactory.decodeResource(getResources(),Picture);
            mItemImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mItemImageView.setImageBitmap(placeholder);

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
        ArrayList<Snap> mSnaps;

        public PhotoAdapter(ArrayList<Snap> snaps){
            mSnaps = snaps;
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
            int R = mSnaps.get(position).getPicture();
            photoHolder.bindDrawable(R, position);
        }

        @Override
        public int getItemCount() {
            return mSnaps.size();
        }
    }

    private void updateUI(){
        RecipeBook book = RecipeBook.getTheRecipeBook();
        ArrayList<Snap> snaps = book.getRecipe(mRecipeID).getSnaps();
        mAdapter = new PhotoAdapter(snaps);
        mPhotoRecyclerView.setAdapter(mAdapter);
    }
}
