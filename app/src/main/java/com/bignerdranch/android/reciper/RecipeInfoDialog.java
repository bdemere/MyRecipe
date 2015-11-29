package com.bignerdranch.android.reciper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bubujay on 11/18/15.
 */
public class RecipeInfoDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.EditCommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.EditCommentDialog.POSITION_Y";

    private String mRecipeName;
    private String mRecipeCategory;

    public RecipeInfoDialog(){
    }

    public static RecipeInfoDialog newInstance(){
        Bundle args = new Bundle();
        RecipeInfoDialog fragment = new RecipeInfoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_info_fragment, container);

        return view;
    }
}
