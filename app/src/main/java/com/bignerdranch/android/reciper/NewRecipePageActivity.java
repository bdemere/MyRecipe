package com.bignerdranch.android.reciper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by bubujay on 11/19/15.
 */
public class NewRecipePageActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return NewRecipeFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
