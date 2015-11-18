package com.bignerdranch.android.reciper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by bubujay on 11/14/15.
 */
public class SnapPagerActivity extends FragmentActivity {
    private static final String EXTRA_SNAP_ID =
            "com.genius.android.reciper.snap_id";

    private ViewPager mViewPager;
    private ArrayList<Snap> mRecipe;

    public static Intent newIntent(Context packageContext, int snapID){
        Intent intent = new Intent(packageContext, SnapPagerActivity.class);
        intent.putExtra(EXTRA_SNAP_ID, snapID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);

        int whichSnap = (int)getIntent().getSerializableExtra(EXTRA_SNAP_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);

        mRecipe = (ArrayList)Recipe.getThisRecipe().getSnaps();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                //Snap snap = mRecipe.get(position);
                return StartPageFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mRecipe.size();
            }
        });
    }

}
