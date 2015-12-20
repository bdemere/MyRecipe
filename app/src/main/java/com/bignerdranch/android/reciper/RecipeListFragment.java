package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bubujay on 11/17/15.
 */
public class RecipeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = (RecyclerView) view
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mRecipeTitle;
        private TextView mRecipeDate;
        private ImageView mImageView;
        private ArrayList<Snap> mSnaps;

        private Recipe mRecipe;

        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.list_item_recipe_title_text_view);
            mRecipeDate = (TextView) itemView.findViewById(R.id.list_item_recipe_date_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_recipe_image_view);
        }

        public void bindRecipe(Recipe recipe) {
            DateFormat f = SimpleDateFormat.getDateInstance();
            mRecipe = recipe;
            mRecipeTitle.setText(mRecipe.getTitle());
            mRecipeDate.setText(f.format(mRecipe.getDate()));

            mSnaps = RecipeBook.getTheRecipeBook(getActivity()).getSnaps(mRecipe.getID());

            if(mSnaps.size() > 1) {
                File mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(mSnaps.get(1));
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
                mImageView.setImageBitmap(RotateBitmap(bitmap,90));
            }
        }

        @Override
        public void onClick(View v) {
            //Intent intent = SnapPagerActivity.newIntent(getActivity(), mRecipe.getID());
            Intent intent = DetailRecipeActivity.newIntent(getActivity(), mRecipe.getID());
            startActivity(intent);
            Log.d("Recipe List", "clicked a recipe " + mRecipe.getTitle());
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<Recipe> mRecipes;
        private TextView mTitle;
        private TextView mDate;
        private ImageView mImageView;

        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            Log.d("recycler", ""+position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
        public void setCrimes(List<Recipe> recipes) {
            mRecipes = recipes;
        }

    }
    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private void updateUI(){
        RecipeBook book = RecipeBook.getTheRecipeBook(getActivity());
        List<Recipe> recipes = book.getRecipes();
        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
    }

}
