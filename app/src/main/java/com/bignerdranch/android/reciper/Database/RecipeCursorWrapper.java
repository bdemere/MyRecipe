package com.bignerdranch.android.reciper.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;

import java.util.Date;
import java.util.UUID;

import static com.bignerdranch.android.reciper.Database.CommentDbSchema.*;
import static com.bignerdranch.android.reciper.Database.RecipeDbSchema.RecipeTable;
import static com.bignerdranch.android.reciper.Database.SnapDbSchema.SnapTable;

/**
 * Created by bimana2 on 11/28/15.
 */
public class RecipeCursorWrapper extends CursorWrapper{
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RecipeTable.Cols.DATE));

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setDate(new Date(date));

        return recipe;
    }

    public Snap getSnap() {
        String uuidString = getString(getColumnIndex(SnapTable.Cols.UUID));
        String parentuuid = getString(getColumnIndex(SnapTable.Cols.PARENT_RECIPE));
        long date = getLong(getColumnIndex(SnapTable.Cols.DATE));

        Snap snap = new Snap(UUID.fromString(uuidString));
        snap.setmParentRecipeID(UUID.fromString(parentuuid));
        snap.setDate(new Date(date));

        return snap;
    }

    public Comment getComment() {
        String uuidString = getString(getColumnIndex(CommentTable.Cols.UUID));
        String parentuuid = getString(getColumnIndex(CommentTable.Cols.PARENT_SNAP));
        long date = getLong(getColumnIndex(CommentTable.Cols.DATE));
        String commentStr = getString(getColumnIndex(CommentTable.Cols.COMMENT));
        float x = getFloat(getColumnIndex(CommentTable.Cols.X_COORD));
        float y = getFloat(getColumnIndex(CommentTable.Cols.Y_COORD));

        Comment comment = new Comment(UUID.fromString(uuidString));
        comment.setParentSnapID(UUID.fromString(parentuuid));
        comment.setDate(new Date(date));
        comment.setCommentsText(commentStr);
        comment.setX(x);
        comment.setY(y);

        return comment;
    }
}
