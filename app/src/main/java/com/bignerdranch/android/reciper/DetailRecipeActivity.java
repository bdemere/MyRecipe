package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

/**
 *  An activity class which hosts DetailRecipeFragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/22/15.
 */
public class DetailRecipeActivity extends SingleFragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.Detail.Recipe_id";

    private UUID RecipeID;

    /**
     * Returns a new intent which fires this activity
     */
    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, DetailRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    /**
     * Returns fragment controlling detailed page of a recipe
     */
    @Override
    protected Fragment createFragment() {
        Log.d("TAG", "created fragment");
        RecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        return DetailRecipeFragment.newInstance(RecipeID);
    }
}
