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
 *  An activity which acts as a wrapper for databse cursor
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/28/15.
 */
public class RecipeCursorWrapper extends CursorWrapper{
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Retrieves all information from columns of a recipe table
     * @return a recipe
     */
    public Recipe getRecipe() {
        // get values from databse
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RecipeTable.Cols.DATE));
        String category = getString(getColumnIndex(RecipeTable.Cols.CATEGORY));
        String servings = getString(getColumnIndex(RecipeTable.Cols.SERVINGS));
        String tags = getString(getColumnIndex(RecipeTable.Cols.TAGS));
        long duration = getLong(getColumnIndex(RecipeTable.Cols.DURATION));
        String difficulty = getString(getColumnIndex(RecipeTable.Cols.DIFFICULTY));
        String snapUuidString = getString(getColumnIndex(RecipeTable.Cols.PRIMARY_SNAP));

        // create the object and set all the values
        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setDate(new Date(date));
        recipe.setCategory(category);
        recipe.setServings(servings);
        recipe.setTags(tags);
        recipe.setDuration(duration);
        recipe.setDifficulty(difficulty);
        recipe.setPrimarySnap(UUID.fromString(snapUuidString));

        return recipe;
    }

    /**
     * Retrieves all information from columns of a snap table
     * @return a snap
     */
    public Snap getSnap() {
        // get values from databse
        String uuidString = getString(getColumnIndex(SnapTable.Cols.UUID));
        String parentuuid = getString(getColumnIndex(SnapTable.Cols.PARENT_RECIPE));
        long date = getLong(getColumnIndex(SnapTable.Cols.DATE));

        // create the object and set all the values
        Snap snap = new Snap(UUID.fromString(uuidString));
        snap.setParentRecipeID(UUID.fromString(parentuuid));
        snap.setDate(new Date(date));

        return snap;
    }

    /**
     * Retrieves all information from columns of a commnet table
     * @return a commnet
     */
    public Comment getComment() {
        // get values from databse
        String uuidString = getString(getColumnIndex(CommentTable.Cols.UUID));
        String parentuuid = getString(getColumnIndex(CommentTable.Cols.PARENT_SNAP));
        long date = getLong(getColumnIndex(CommentTable.Cols.DATE));
        String commentStr = getString(getColumnIndex(CommentTable.Cols.COMMENT));
        float x = getFloat(getColumnIndex(CommentTable.Cols.X_COORD));
        float y = getFloat(getColumnIndex(CommentTable.Cols.Y_COORD));

        //c reate the object and set all the values
        Comment comment = new Comment(UUID.fromString(uuidString));
        comment.setParentSnapID(UUID.fromString(parentuuid));
        comment.setDate(new Date(date));
        comment.setCommentsText(commentStr);
        comment.setX(x);
        comment.setY(y);

        return comment;
    }
}
