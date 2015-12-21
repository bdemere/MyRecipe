package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
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
import com.bignerdranch.android.reciper.RecipeBook;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *  An activity class which hosts DetailRecipeFragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/17/15.
 */
public class RecipeListFragment extends Fragment {

    // member variables
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // initialize member variables
        mRecipeRecyclerView = (RecyclerView) view
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetRecipeList().execute();

        return view;
    }

    /**
     * ViewHolder for recycler view that lists recipes
     */
    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // member varialbes
        private TextView mRecipeTitle;
        private TextView mRecipeDate;
        private ImageView mImageView;
        private ArrayList<Snap> mSnaps;
        private Snap snap;
        private File mPhotoFile;
        private Recipe mRecipe;

        /**
         * Constructor
         * @param itemView
         */
        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.list_item_recipe_title_text_view);
            mRecipeDate = (TextView) itemView.findViewById(R.id.list_item_recipe_date_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_recipe_image_view);
        }

        /**
         * Binds recipe data
         * @param recipe recipe instance
         */
        public void bindRecipe(Recipe recipe) {
            DateFormat f = SimpleDateFormat.getDateInstance();
            // set recipe content to views
            mRecipe = recipe;
            mRecipeTitle.setText(mRecipe.getTitle());
            mRecipeDate.setText(f.format(mRecipe.getDate()));
            new GetSnapList().execute();

        }

        @Override
        public void onClick(View v) {
            // launch detail page on click
            Intent intent = DetailRecipeActivity.newIntent(getActivity(), mRecipe.getID());
            startActivity(intent);
        }


        /**
         * Async task for gets snaps list in the background
         */
        private class GetSnapList extends
                AsyncTask<Void, String, ArrayList<Snap>> {

            @Override
            protected ArrayList<Snap> doInBackground(Void... params) {
                RecipeBook book = RecipeBook.getTheRecipeBook(getActivity());
                ArrayList<Snap> snaps = book.getSnaps(mRecipe.getID());
                return snaps;
            }

            @Override
            protected void onPostExecute(ArrayList<Snap> result) {
                super.onPostExecute(result);
                mSnaps = result;
                if(mSnaps.size() > 1) {
                    snap = mSnaps.get(1);
                    mPhotoFile = RecipeBook.getTheRecipeBook(getActivity()).getPhotoFile(snap);
                    mImageView.setImageBitmap(PictureUtils.RotateBitmap(BitmapFactory.decodeFile(mPhotoFile.getPath()), 90));
                }
            }
        }
    }

    /**
     * Adapter for recycler view that list recipes
     */
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<Recipe> mRecipes;

        /**
         * Constructor
         * @param recipes list of recipes
         */
        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // creates a new view holder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            // binds view  hodler with data
            Recipe recipe = mRecipes.get(position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

    }

    /**
     * Async task for loading recipes in the backgorund
     */
    private class GetRecipeList extends
            AsyncTask<Void, String, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... params) {
            // get recipes from database
            RecipeBook book = RecipeBook.getTheRecipeBook(getActivity());
            List<Recipe> recipes = book.getRecipes();
            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> result) {
            super.onPostExecute(result);
            // create a new adapter and attach it to the recycler view
            mRecipes = result;
            mAdapter = new RecipeAdapter(result);
            mRecipeRecyclerView.setAdapter(mAdapter);
        }
    }


}
