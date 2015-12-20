package com.bignerdranch.android.reciper.Dialogs;

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
import android.widget.Toast;

import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/18/15.
 */
public class CommentDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.CommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    private EditText mComment;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private Recipe mRecipe;
    private ArrayList<Snap> mSnaps;
    private Button mAddButton;
    private Snap mCurrentSnap;
    public CommentDialog(){
    }

    public static CommentDialog newInstance(float positionX, float positionY, int snapPosition, UUID recipeId){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        CommentDialog fragment = new CommentDialog();
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
        sendResult(Activity.RESULT_OK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_dialog_fragment, container);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        mComment = (EditText) view.findViewById(R.id.xy_comment);
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mComment.getText().toString().length() != 0)
                    mAddButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAddButton = (Button) view.findViewById(R.id.add_comment_button);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theComment = mComment.getText().toString();
                Log.d("Dialog", theComment);
                if(theComment.length() != 0) {
                    //Snap latestSnap = RecipeBook.getTheRecipeBook(getContext()).getLatestRecipe().getSnap(snapPos);
                    Snap latestSnap = mCurrentSnap;
                    Comment result = latestSnap.searchComments(mX, mY);

                    Toast.makeText(getActivity(), "New Comment", Toast.LENGTH_SHORT).show();
                    Comment comment = latestSnap.newComment(mCurrentSnap.getId());
                    comment.setX(mX);
                    comment.setY(mY);
                    mTheBook.addComment(comment);
                    //Comment newestComment = latestSnap.getLatestComment();
                    //comment.addTextComment(mComment.getText().toString());
                    mTheBook.addCommentText(theComment, comment);
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