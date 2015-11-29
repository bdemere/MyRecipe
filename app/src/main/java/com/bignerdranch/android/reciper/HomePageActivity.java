package com.bignerdranch.android.reciper;

import android.support.v4.app.Fragment;

public class HomePageActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return HomePageFragment.newInstance(1);
    }

}
