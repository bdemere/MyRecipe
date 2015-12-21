package com.bignerdranch.android.reciper.SnapControllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.ArrayList;
import java.util.UUID;

/**
 *  An activity class that hosts new SnapPager Fragment
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/14/15.
 */
public class NewRecipeSnapPagerActivity extends FragmentActivity {
    private static final String EXTRA_RECIPE_ID = "com.genius.android.reciper.snap_id";

    // member variables
    private ViewPager mViewPager;
    private UUID mRecipeID;
    private ArrayList<Snap> mRecipeSnaps;
    private RecipeAdapter mAdapter;

    /**
     * Returns a new intent which fires this activity
     */
    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, NewRecipeSnapPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);

        // initialize member variables
        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);
        mRecipeID = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        mRecipeSnaps = RecipeBook.getTheRecipeBook(this).getSnaps(mRecipeID);

        // set margin between images
        mViewPager.setPageMargin(60);

        // create fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //set adapter to the viewPager
        mAdapter = new RecipeAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * Update all fragments when data set changes
     */
    public void update() {
        mRecipeSnaps = RecipeBook.getTheRecipeBook(this).getSnaps(mRecipeID);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    /**
     * Adapter from ViewPager, creates new snapFragments
     */
    public class RecipeAdapter extends FragmentStatePagerAdapter {

        public RecipeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            boolean isCamera = false;
            // switch view from a page showing an image into a page showing a camera button
            if(position == mRecipeSnaps.size() - 1)
                isCamera = !isCamera;

            return NewSnapFragment.newInstance(mRecipeSnaps.size() - 1 - position, mRecipeID, isCamera);
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mRecipeSnaps.size();
        }
    }

}