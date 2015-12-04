package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import java.util.UUID;

/**
 * Created by bimana2 on 12/2/15.
 */
public class RecipeInfoFormActivity extends SingleFragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.Detail.Recipe_id";
    private static final String IS_NEW_RECIPE =
            "com.genius.android.reciper.Detail.Is_new";

    public static Intent newIntent(Context packageContext, UUID recipeID, boolean isNew){
        Intent intent = new Intent(packageContext, RecipeInfoFormActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        intent.putExtra(IS_NEW_RECIPE, isNew);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        UUID recipeId = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        boolean isNew = (boolean) getIntent().getSerializableExtra(IS_NEW_RECIPE);
        return RecipeInfoFormFragment.newInstance(recipeId, isNew);
    }

}

