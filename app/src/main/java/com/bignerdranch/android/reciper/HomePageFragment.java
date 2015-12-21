package com.bignerdranch.android.reciper;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.SnapControllers.NewRecipeSnapPagerActivity;
import com.bignerdranch.android.reciper.Models.Recipe;

/**
 *  Fragment which controls the homepage
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 1/13/15.
 */
public class HomePageFragment extends Fragment {

    final static String SNAP_ID = "com.genius.android.reciper";

    // member variables
    private Button mNewRecipe;
    private Button mAllRecipes;
    private RecipeBook mTheBook;


    public static HomePageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable(SNAP_ID, position);

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_page, container, false);

        // initialize member variables
        mNewRecipe = (Button)v.findViewById(R.id.new_recipe_button);
        mAllRecipes = (Button)v.findViewById(R.id.all_recipes_button);
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());

        //open a new recipe page
        mNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new recipe
                Recipe recipe = new Recipe();
                recipe.setTitle("Recipe");
                mTheBook.addRecipe(recipe);

                // add a dummy snap to the recipe for the 'Add snap' page
                Snap snap = Recipe.newSnap(recipe.getID());
                mTheBook.addSnap(snap);
                Timer.getTimer(getActivity()).start();
                Intent intent = NewRecipeSnapPagerActivity.newIntent(getActivity(), recipe.getID());
                startActivity(intent);
            }
        });

        // open list of all past recipes
        mAllRecipes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeListActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
