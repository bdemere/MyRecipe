package com.bignerdranch.android.reciper.SnapControllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.data.RecipeBook;
import com.bignerdranch.android.reciper.data.Snap;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class SnapPagerActivity extends FragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.snap_id";
    private static final String EXTRA_START_POSITION =
            "com.genius.android.reciper.SnapPager.snap_id";

    private ViewPager mViewPager;
    private UUID mRecipeID;
    private int mStartPos;
    private ArrayList<Snap> mRecipeSnaps;

    public static Intent newIntent(Context packageContext, UUID recipeID, int startPosition){
        Intent intent = new Intent(packageContext, SnapPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        intent.putExtra(EXTRA_START_POSITION, startPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);
        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);

        mRecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        mStartPos = (int)getIntent().getSerializableExtra(EXTRA_START_POSITION);

        mRecipeSnaps = (ArrayList) RecipeBook.getTheRecipeBook(this).getRecipe(mRecipeID).getSnaps();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                //Snap snap = mRecipe.get(position);
                return SnapFragment.newInstance(mRecipeSnaps.size() - 1 - position, mRecipeID);
            }

            @Override
            public int getCount() {
                return mRecipeSnaps.size() - 1;
            }
        });
        mViewPager.setCurrentItem(mStartPos);
    }

}
