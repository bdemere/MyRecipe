package com.bignerdranch.android.reciper.SnapControllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.ArrayList;
import java.util.UUID;

/**
 *  An activity class that hosts SnapPager Fragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/14/15.
 */
public class SnapPagerActivity extends FragmentActivity {

    private static final String EXTRA_RECIPE_ID = "com.genius.android.reciper.snap_id";
    private static final String EXTRA_START_POSITION = "com.genius.android.reciper.SnapPager.snap_id";

    //member variables
    private ViewPager mViewPager;
    private UUID mRecipeID;
    private int mStartPos;
    private ArrayList<Snap> mRecipeSnaps;

    /**
     * Returns a new intent which fires this activity
     */
    public static Intent newIntent(Context packageContext, UUID recipeID, int startPosition){
        Intent intent = new Intent(packageContext, SnapPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        intent.putExtra(EXTRA_START_POSITION, startPosition); // snap clicked on
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);

        // initialize member variables
        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);
        mRecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        mStartPos = (int)getIntent().getSerializableExtra(EXTRA_START_POSITION);
        mRecipeSnaps = RecipeBook.getTheRecipeBook(this).getSnaps(mRecipeID);

        // leaves margin between images
        mViewPager.setPageMargin(60);

        // create fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // set adapter to the viewpager
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return SnapFragment.newInstance(mRecipeSnaps.size() - 1 - position, mRecipeID);
            }

            @Override
            public int getCount() {
                return mRecipeSnaps.size() - 1;
            }
        });

        // set the current item to be the one clicked on
        mViewPager.setCurrentItem(mStartPos);
    }

}
