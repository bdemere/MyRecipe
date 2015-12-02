package com.bignerdranch.android.reciper.SnapControllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class NewRecipeSnapPagerActivity extends FragmentActivity {
    private static final String EXTRA_RECIPE_ID =
            "com.genius.android.reciper.snap_id";

    private ViewPager mViewPager;
    private UUID mRecipeID;
    private Recipe mRecipe;
    private ArrayList<Snap> mRecipeSnaps;
    private RecipeAdapter mAdapter;

    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, NewRecipeSnapPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_snap_pager_view_pager);
        mViewPager.setPageMargin(60);

        mRecipeID = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        mRecipe = RecipeBook.getTheRecipeBook(this).getRecipe(mRecipeID);
        mRecipeSnaps = RecipeBook.getTheRecipeBook(this).getSnaps(mRecipeID);

        Log.d("TAG  " , "Size:: "+ RecipeBook.getTheRecipeBook(getBaseContext()).getRecipes().size());
        Log.d("TAG  " , "ID recived:: "+ mRecipeID);
        Log.d("TAG", "Number of snaps: " + mRecipeSnaps.size());

        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdapter = new RecipeAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);
    }

    public void update(){
        mRecipeSnaps = RecipeBook.getTheRecipeBook(this).getSnaps(mRecipeID);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public class RecipeAdapter extends FragmentStatePagerAdapter {
        public RecipeAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Log.d("I'm here", "" + position);
            Log.d("I'm Here", "" + mRecipeSnaps.size());

            boolean isCamera = false;
            if(position == mRecipeSnaps.size() - 1)
                isCamera = !isCamera; //switch view from a page showing an image into a page showing a camera button

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