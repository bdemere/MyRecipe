package com.bignerdranch.android.reciper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

/**
 *  An activity class which hosts HomePageFragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/22/15.
 */
public class HomePageActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // modify actionBar appearance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    /**
     * Creates and returns a HomePageFragment
     */
    @Override
    protected Fragment createFragment() {
        return HomePageFragment.newInstance(1);
    }

}
