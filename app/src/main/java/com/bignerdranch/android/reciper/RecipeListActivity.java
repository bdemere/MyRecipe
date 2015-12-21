package com.bignerdranch.android.reciper;

import android.support.v4.app.Fragment;

/**
 *  An activity class which hosts list of all past recipes
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/17/15.
 */
public class RecipeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}
