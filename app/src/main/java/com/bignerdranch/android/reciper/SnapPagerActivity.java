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
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class SnapPagerActivity extends FragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.snap_id";

    private ViewPager mViewPager;
    private UUID mRecipeID;
    private ArrayList<Snap> mRecipeSnaps;

    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, SnapPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);
        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);

        mRecipeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECIPE_ID);

        mRecipeSnaps = (ArrayList)RecipeBook.getTheRecipeBook().getRecipe(mRecipeID).getSnaps();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                //Snap snap = mRecipe.get(position);
                return SnapFragment.newInstance(position, mRecipeID);
            }

            @Override
            public int getCount() {
                return mRecipeSnaps.size();
            }
        });
    }

}
