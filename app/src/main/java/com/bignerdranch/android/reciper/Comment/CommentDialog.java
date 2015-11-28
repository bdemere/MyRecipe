package com.bignerdranch.android.reciper.Comment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.data.Comment;
import com.bignerdranch.android.reciper.data.RecipeBook;
import com.bignerdranch.android.reciper.data.Snap;

/**
 * Created by bubujay on 11/18/15.
 */
public class CommentDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.CommentDialog.SNAP_POSITION";

    private EditText mComment;
    private float mX;
    private float mY;
    private int snapPos;

    public CommentDialog(){
    }

    public static CommentDialog newInstance(float positionX, float positionY, int snapPosition){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        CommentDialog fragment = new CommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mX = (float)getArguments().getSerializable(POSITION_X);
        mY = (float)getArguments().getSerializable(POSITION_Y);
        snapPos = (int)getArguments().getSerializable(SNAP_POSITION);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(1000,600);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("Dialog", mComment.getText().toString());
        Snap latestSnap = RecipeBook.getTheRecipeBook(getContext()).getLatestRecipe().getSnap(snapPos);

        Comment result = latestSnap.searchComments(mX, mY);

        if(result == null) {
            Toast.makeText(getActivity(), "New Comment", Toast.LENGTH_SHORT).show();
            latestSnap.addComment();
            Comment newestComment = latestSnap.getLatestComment();
            newestComment.addTextComment(mComment.getText().toString());
            newestComment.setX(mX);
            newestComment.setY(mY);
        }
        else {
            result.addTextComment(mComment.getText().toString());
            Toast.makeText(getActivity(), "Adding a comment", Toast.LENGTH_SHORT).show();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comment_dialog_fragment, container);
        mComment = (EditText) view.findViewById(R.id.xy_comment);

        //Window window = getDialog().getWindow();
        // set "origin" to top left corner
        //getDialog().setTitle("Comment");
        //window.setGravity(Gravity.TOP | Gravity.LEFT);
        /*WindowManager.LayoutParams params = window.getAttributes();
        params.x = Math.round(mX);
        params.y = Math.round(mY);
        window.setAttributes(params);*/

        return view;
    }
}
