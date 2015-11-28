package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.reciper.data.Recipe;

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

        private Recipe mRecipe;

        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.list_item_recipe_title_text_view);
            mRecipeDate = (TextView) itemView.findViewById(R.id.list_item_recipe_date_text_view);
        }

        public void bindRecipe(Recipe recipe) {
            mRecipe = recipe;
            mRecipeTitle.setText(mRecipe.getTitle());
            mRecipeDate.setText(mRecipe.getDate().toString());
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

    private void updateUI(){
        RecipeBook book = RecipeBook.getTheRecipeBook(getActivity());
        List<Recipe> recipes = book.getTheRecipes();
        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
    }

}
