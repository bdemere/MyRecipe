package com.bignerdranch.android.reciper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by bubujay on 11/18/15.
 */
public class CommentDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";

    private EditText mComment;
    private float mX;
    private float mY;

    public CommentDialog(){
    }

    public static CommentDialog newInstance(float positionX, float positionY){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        CommentDialog fragment = new CommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mX = (float)getArguments().getSerializable(POSITION_X);
        mY = (float)getArguments().getSerializable(POSITION_Y);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comment_dialog_fragment, container);
        mComment = (EditText) view.findViewById(R.id.xy_comment);
        Window window = getDialog().getWindow();
        // set "origin" to top left corner
        getDialog().setTitle("Comment");
        window.setGravity(Gravity.TOP | Gravity.LEFT);

        WindowManager.LayoutParams params = window.getAttributes();
        params.x = Math.round(mX);
        params.y = Math.round(mY);
        window.setAttributes(params);

        return view;
    }
}
