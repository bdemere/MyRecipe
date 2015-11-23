package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by bubujay on 11/22/15.
 */
public class DetailRecipeActivity extends SingleFragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.Detail.Recipe_id";

    private UUID RecipeID;

    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, DetailRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
    }

    @Override
    protected Fragment createFragment() {
        return DetailRecipeFragment.newInstance(RecipeID);
    }
}
