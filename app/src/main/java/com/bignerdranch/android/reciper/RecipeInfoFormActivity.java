package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import java.util.UUID;

/**
 *  An activity which hosts info form page fragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 12/02/15.
 */
public class RecipeInfoFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.Detail.Recipe_id";
    private static final String IS_NEW_RECIPE =
            "com.genius.android.reciper.Detail.Is_new";

    /**
     * Returns a new intent which fires this activity
     * @param isNew determines if it is a new recipe or if existing recipe is to be edited
     */
    public static Intent newIntent(Context packageContext, UUID recipeID, boolean isNew){
        Intent intent = new Intent(packageContext, RecipeInfoFormActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        intent.putExtra(IS_NEW_RECIPE, isNew);
        return intent;
    }

    /**
     * Returns fragment controlling recipe info form page
     */
    @Override
    protected Fragment createFragment() {
        UUID recipeId = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        boolean isNew = (boolean) getIntent().getSerializableExtra(IS_NEW_RECIPE);
        return RecipeInfoFormFragment.newInstance(recipeId, isNew);
    }

}

