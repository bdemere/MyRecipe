package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

/**
 *  An activity class which hosts DetailRecipeFragment(fragment)
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/22/15.
 */
public class DetailRecipeActivity extends SingleFragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.Detail.Recipe_id";

    private UUID RecipeID;

    /*
        * a method to return a new intent
        * @return   an intent which fires this this activity
     */
    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, DetailRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    /*
        * a method return DetailRecipeFragment
        * @return  a DetailRecipeFragment fragment controlling a page
        *          of a certain recipe
     */
    @Override
    protected Fragment createFragment() {
        Log.d("TAG", "created fragment");
        RecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        return DetailRecipeFragment.newInstance(RecipeID);
    }
}
