package com.bignerdranch.android.reciper;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.bignerdranch.android.reciper.Models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by bimana2 on 12/2/15.
 */
public class RecipeInfoFormFragment extends Fragment {
    private static final String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";
    private static final String IS_NEW = "com.genius.android.reciper.IS_NEW";

    private UUID mRecipeID;
    private Recipe mRecipe;

    private Button mSaveButton;
    private EditText mTitle;
    private Spinner mSpinnerCategory;
    private EditText mServings;
    private Spinner mSpinnerLevel;
    private EditText mTags;

    private boolean isNew;

    public static RecipeInfoFormFragment newInstance(UUID RecipeID, boolean isNew) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ID, RecipeID);
        args.putSerializable(IS_NEW, isNew);
        RecipeInfoFormFragment fragment = new RecipeInfoFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNew = (boolean) getArguments().getSerializable(IS_NEW);
        mRecipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mRecipe = RecipeBook.getTheRecipeBook(getActivity()).getRecipe(mRecipeID);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_info_fragment, container, false);
        mSaveButton = (Button) v.findViewById(R.id.save_recipe_button);
        mTitle = (EditText) v.findViewById(R.id.title_edit_text);
        mSpinnerCategory = (Spinner) v.findViewById(R.id.spinner_category);
        mServings = (EditText) v.findViewById(R.id.servings_edit_text);
        mSpinnerLevel = (Spinner) v.findViewById(R.id.spinner_level);
        mTags = (EditText) v.findViewById(R.id.tags_edit_text);

        String[] category_array = getResources().getStringArray(R.array.category_arrays);
        String[] level_array = getResources().getStringArray(R.array.difficulty_arrays);
        ArrayList<String> al = new ArrayList(Arrays.asList(category_array));
        ArrayList<String> al2 = new ArrayList(Arrays.asList(level_array));

        if(!isNew) {
            mTitle.setText(mRecipe.getTitle());
            mSpinnerCategory.setSelection(al.indexOf(mRecipe.getCategory()));
            mServings.setText(mRecipe.getServings());
            mTags.setText(mRecipe.getTags());
            mSpinnerLevel.setSelection(al2.indexOf(mRecipe.getDifficulty()));
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNew) {
                    Timer.getTimer(getActivity()).stop();
                    mRecipe.setDuration(Timer.getTimer(getActivity()).durationMinutes());
                    Timer.getTimer(getActivity()).reset();
                }
                mRecipe.setTitle(mTitle.getText().toString());
                mRecipe.setCategory(mSpinnerCategory.getSelectedItem().toString());
                mRecipe.setServings(mServings.getText().toString());
                mRecipe.setTags(mTags.getText().toString());
                mRecipe.setDifficulty(mSpinnerLevel.getSelectedItem().toString());

                RecipeBook.getTheRecipeBook(getActivity()).updateRecipe(mRecipe);
                Intent intent = DetailRecipeActivity.newIntent(getActivity(), mRecipeID);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }
}
