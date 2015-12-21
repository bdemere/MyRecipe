package com.bignerdranch.android.reciper.CommentDialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.GridLayout;
import android.widget.Toast;

import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.RecipeBook;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  Dialog for adding or editing a comment at a previous XY location
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/18/2015.
 */
public class EditCommentDialog extends DialogFragment {

    final static String POSITION_X = "com.genius.android.reciper.CommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.CommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.CommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.RECIPE_ID";

    // member variables
    private RecyclerView mCommentRecyclerView;
    private RecipeAdapter mAdapter;
    private Comment comment;
    private EditText mComment;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private RecipeBook mTheBook;
    private ArrayList<Snap> mSnaps;
    private Button mAddButton;
    private Snap mCurrentSnap;
    public EditCommentDialog(){
    }

    /**
     * Creates a new instance of this fragment
     */
    public static EditCommentDialog newInstance(float positionX, float positionY, int snapPosition, UUID recipeId){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        EditCommentDialog fragment = new EditCommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // initialize member variables
        mX = (float)getArguments().getSerializable(POSITION_X);
        mY = (float)getArguments().getSerializable(POSITION_Y);
        snapPos = (int)getArguments().getSerializable(SNAP_POSITION);
        recipeID = (UUID)getArguments().getSerializable(RECIPE_ID);
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapPos);
        comment = mCurrentSnap.searchComments(mX, mY);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        // set dialog window size based on the width and height of the screen
        Window window = getDialog().getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        window.setLayout((5 * width)/6, GridLayout.LayoutParams.WRAP_CONTENT);
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
        View view = inflater.inflate(R.layout.edit_dialog_fragment, container);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        // get comment editText view
        mComment = (EditText) view.findViewById(R.id.xy_comment);

        // enable the add button only when user starts writing comment
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

        // get add button, initially disabled
        mAddButton = (Button) view.findViewById(R.id.add_comment_button);
        mAddButton.setEnabled(false);

        // create a new comment and store in databse
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // search for comments near current xy location
                Comment result = mCurrentSnap.searchComments(mX, mY);
                // add new or edited comments texts to existing comment object
                mTheBook.addCommentText(mComment.getText().toString(), result);
                Toast.makeText(getActivity(), "Adding a comment", Toast.LENGTH_SHORT).show();
                // send result to parent activity and close fragment
                sendResult(Activity.RESULT_OK);
                dismiss();

            }
        });

        mCommentRecyclerView = (RecyclerView)view.findViewById(R.id.edit_dialog_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();
        return view;
    }

    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EditText aComment;
        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            aComment = (EditText) itemView.findViewById(R.id.edit_list_item_comment_text_view);
        }

        public void bindRecipe(String tComment, final int position) {
            aComment.setText(tComment);
            aComment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    comment.editTextComment(position, aComment.getText().toString());
                    mTheBook.updateComment(comment);
                    mAddButton.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<String> textComments;

        public RecipeAdapter(List<String> tComments) {
            textComments = tComments;
            Log.d("TAG", "Number of comments: " + textComments.size());
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.edit_comment_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            String tComment = textComments.get(position);
            Log.d("recycler", "" + position);
            holder.bindRecipe(tComment,position);
        }

        @Override
        public int getItemCount() {
            return textComments.size();
        }
    }

    /**
     * Updates UI by attacing adapter to the RecyclerView
     */
    private void updateUI(){
        List<String> comments = comment.getCommentsList();
        mAdapter = new RecipeAdapter(comments);
        mCommentRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Sends result to parent activity
     * @param resultCode code of result to be send
     */
    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}