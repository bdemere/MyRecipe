package com.bignerdranch.android.reciper;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.bignerdranch.android.reciper.Models.Recipe;

import java.util.UUID;

/**
 * Created by bimana2 on 12/2/15.
 */
public class RecipeInfoFormFragment extends Fragment {
    private static final String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    private UUID mRecipeID;

    public static RecipeInfoFormFragment newInstance(UUID RecipeID) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ID, RecipeID);
        RecipeInfoFormFragment fragment = new RecipeInfoFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_info_fragment, container, false);

        return v;
    }
}
