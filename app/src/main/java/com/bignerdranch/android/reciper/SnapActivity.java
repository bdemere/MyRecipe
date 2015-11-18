package com.bignerdranch.android.reciper;

import android.support.v4.app.Fragment;

public class SnapActivity extends SingleFragmentActivity {

    /*private static final String SNAP_ID =
            "com.bignerdranch.android.criminalintent.crime_id";*/

    /*public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, SnapActivity.class);
        intent.putExtra(SNAP_ID, crimeId);
        return intent;
    }*/

    @Override
    protected Fragment createFragment() {
        return StartPageFragment.newInstance(1);
    }

}
