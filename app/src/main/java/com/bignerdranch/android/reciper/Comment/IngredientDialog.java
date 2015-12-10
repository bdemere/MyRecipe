package com.bignerdranch.android.reciper.Comment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.android.reciper.Models.Ingredient;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/18/15.
 */
public class IngredientDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.CommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    private EditText mIngredient;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private Button mAddButton;
    private Snap mCurrentSnap;
    public IngredientDialog(){
    }

    public static IngredientDialog newInstance(float positionX, float positionY, int snapPosition, UUID recipeId){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        IngredientDialog fragment = new IngredientDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mX = (float)getArguments().getSerializable(POSITION_X);
        mY = (float)getArguments().getSerializable(POSITION_Y);
        snapPos = (int)getArguments().getSerializable(SNAP_POSITION);

        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mRecipe = mTheBook.getRecipe(recipeID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapPos);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        window.setLayout((3*width)/4,height/4);
        window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_dialog_fragment, container);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        mIngredient = (EditText) view.findViewById(R.id.xy_ingredient);
        mIngredient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mIngredient.getText().toString().length() != 0)
                    mAddButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAddButton = (Button) view.findViewById(R.id.ingredient_add_button);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theIngredient = mIngredient.getText().toString();
                Log.d("Dialog", theIngredient);
                if (theIngredient.length() != 0) {
                    //Snap latestSnap = RecipeBook.getTheRecipeBook(getContext()).getLatestRecipe().getSnap(snapPos);
                    Snap latestSnap = mCurrentSnap;
                    Ingredient i = new Ingredient();
                    i.setCoordinate(mX, mY);
                    i.setIngredient(theIngredient);
                    latestSnap.addIngredient(i);
                    for(int j = 0; j < latestSnap.getIngredientList().size(); j++)
                        Log.d("ingredienTtest", latestSnap.getIngredientList().get(j).getIngredient());
                }
                sendResult(Activity.RESULT_OK);
                dismiss();

            }
        });


        return view;
    }

    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}