package com.bignerdranch.android.reciper.Comment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bignerdranch.android.reciper.R;
import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.RecipeBook;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bubujay on 11/18/15.
 */
public class AddCommentDialog extends DialogFragment {
    final static String POSITION_X = "com.genius.android.reciper.AddCommentDialog.POSITION_X";
    final static String POSITION_Y = "com.genius.android.reciper.AddCommentDialog.POSITION_Y";
    final static String SNAP_POSITION = "com.genius.android.reciper.AddCommentDialog.SNAP_POSITION";
    final static String RECIPE_ID = "com.genius.android.reciper.AddCommentDialog.RECIPE_ID";

    private RecyclerView mCommentRecyclerView;
    private RecipeAdapter mAdapter;
    private TextView mComment;
    private float mX;
    private float mY;
    private int snapPos;
    private UUID recipeID;
    private Comment comment;
    private RecipeBook mTheBook;

    public AddCommentDialog(){
    }

    public static AddCommentDialog newInstance(float positionX, float positionY, UUID recipeId, int snapPosition){
        Bundle args = new Bundle();
        args.putSerializable(POSITION_X, positionX);
        args.putSerializable(POSITION_Y, positionY);
        args.putSerializable(SNAP_POSITION, snapPosition);
        args.putSerializable(RECIPE_ID, recipeId);
        AddCommentDialog fragment = new AddCommentDialog();
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
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(700, 750);
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
        mComment = (TextView) view.findViewById(R.id.comment_text);

        Snap currentSnap = mTheBook.getSnaps(recipeID).get(snapPos);
        ArrayList<Comment> comments = currentSnap.getComments();

        comment = currentSnap.searchComments(mX, mY);

        if(comment == null)
            mComment.setText("no Comment");
        else
            mComment.setText(comment.getTextComment());

        Window window = getDialog().getWindow();
        // set "origin" to top left corner
        //getDialog().setTitle("Comment");
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = Math.round(mX);
        params.y = Math.round(mY);
        window.setAttributes(params);

        return view;
    }


    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView aComment;
        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            aComment = (TextView) itemView.findViewById(R.id.list_item_comment_text_view);
        }

        public void bindRecipe(String tComment) {
            aComment.setText(tComment);
        }

        @Override
        public void onClick(View v) {
            Log.d("comment List", "clicked a comment ");
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<String> textComments;

        public RecipeAdapter(List<String> tComments) {
            textComments = tComments;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.comment_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            String tComment = textComments.get(position);
            Log.d("recycler", "" + position);
            holder.bindRecipe(tComment);
        }

        @Override
        public int getItemCount() {
            return textComments.size();
        }
    }

    private void updateUI(){
        List<String> comments = comment.getTextComments();
        mAdapter = new RecipeAdapter(comments);
        mCommentRecyclerView.setAdapter(mAdapter);
    }
}
