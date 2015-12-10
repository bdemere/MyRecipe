package com.bignerdranch.android.reciper;

import android.support.v4.app.Fragment;

/**
 * Created by bubujay on 11/17/15.
 */
public class RecipeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}
