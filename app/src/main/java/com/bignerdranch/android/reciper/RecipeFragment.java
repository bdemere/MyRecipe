package com.bignerdranch.android.reciper;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remember on 11/15/2015.
 */
public class RecipeFragment extends Fragment {
    private BaseAdapter mAdapter;
    private RecyclerView mPhotoRecyclerView;

    public static RecipeFragment newInstance() {
        RecipeFragment fragment = new RecipeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_fragment, container, false);

        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.crime_recycler_view);

        LinearLayoutManager layoutManager;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }

        mPhotoRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        mPhotoRecyclerView.setAdapter(new PhotoAdapter());

    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView
                    .findViewById(R.id.text);
        }

        public void bindDrawable() {
            Drawable placeholder = getResources().getDrawable(R.drawable.bbq);
            mItemImageView.setImageDrawable(placeholder);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            photoHolder.bindDrawable();
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
