package com.bignerdranch.android.reciper;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by bubujay on 11/13/15.
 */
public class HomePageFragment extends Fragment {

    final static String SNAP_ID = "com.genius.android.reciper";
    public Button mNewRecipe;
    public Button mAllRecipes;

    public static HomePageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable(SNAP_ID, position);

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_page, container, false);
        mNewRecipe = (Button)v.findViewById(R.id.new_recipe_button);
        mAllRecipes = (Button)v.findViewById(R.id.all_recipes_button);

        mAllRecipes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeListActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
