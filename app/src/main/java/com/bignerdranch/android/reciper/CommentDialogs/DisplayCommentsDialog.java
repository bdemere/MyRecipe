package com.bignerdranch.android.reciper.CommentDialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  Dialog displayed comments on already saved recipe snaps
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/18/2015.
 */
public class DisplayCommentsDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.DisplayCommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.DisplayCommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.DisplayCommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.DisplayCommentDialog.RECIPE_ID";

    // member variables
    private RecyclerView mCommentRecyclerView;
    private RecipeAdapter mAdapter;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private Comment comment;
    private RecipeBook mTheBook;
    private ArrayList<Snap> mSnaps;
    private Snap mCurrentSnap;

    public DisplayCommentsDialog(){

    }

    /**
     * Creates a new instance of this fragment
     */
    public static DisplayCommentsDialog newInstance(float positionX, float positionY, UUID recipeId, int snapPosition){
        // builds necessary arguments to create fragment
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        DisplayCommentsDialog fragment = new DisplayCommentsDialog();
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
        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mSnaps = mTheBook.getSnaps(recipeID);
        mCurrentSnap = mSnaps.get(snapPos);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        // set dialog window size based on the width and height of the screen
        Window window = getDialog().getWindow();
        int height = 350 + (comment.getCommentsList().size() - 1) * 100;
        int maxWidth = 800;
        int maxHeight = 800;
        if(height < maxHeight) {
            window.setLayout(maxWidth, height);
        } else {
            window.setLayout(maxWidth, maxHeight);
        }
        //window.setGravity(Gravity.CENTER);
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

        View view = inflater.inflate(R.layout.display_comment_dialog_fragment, container);
        comment = mCurrentSnap.searchComments(mX, mY);

        // determine where in the screen the dialog is going to be located
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = Math.round(mX);
        params.y = Math.round(mY);
        window.setAttributes(params);

        mCommentRecyclerView = (RecyclerView)view.findViewById(R.id.included_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();

        return view;
    }


    /**
     * Viewholder for recycler view that list comments
     */
    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView aComment;

        /**
         * Constructor
         */
        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            aComment = (TextView) itemView.findViewById(R.id.list_item_comment_text_view);
        }

        /**
         * Sets comment textView content
         * @param tComment
         */
        public void bindRecipe(String tComment) {
            aComment.setText(tComment);
        }

        @Override
        public void onClick(View v) {
            Log.d("comment List", "clicked a comment ");
        }
    }

    /**
     * Adapter for recycler view that list comments
     */
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        // member variable
        private List<String> textComments;

        /**
         * Constructor
         * @param tComments list of comment objects
         */
        public RecipeAdapter(List<String> tComments) {
            textComments = tComments;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflates layout
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.comment_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            // bind layout with data
            String tComment = textComments.get(position);
            holder.bindRecipe(tComment);
        }

        @Override
        public int getItemCount() {
            return textComments.size();
        }
    }

    /**
     * Updates UI by attaching adpater to the RecyclerView
     */
    private void updateUI(){
        List<String> comments = comment.getCommentsList();
        mAdapter = new RecipeAdapter(comments);
        mCommentRecyclerView.setAdapter(mAdapter);
    }
}
