package com.bignerdranch.android.reciper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.reciper.Models.Comment;
import com.bignerdranch.android.reciper.Models.Recipe;
import com.bignerdranch.android.reciper.Models.Snap;
import com.bignerdranch.android.reciper.Database.DatabaseHelper;
import com.bignerdranch.android.reciper.Database.RecipeCursorWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static com.bignerdranch.android.reciper.Database.CommentDbSchema.*;
import static com.bignerdranch.android.reciper.Database.RecipeDbSchema.*;
import static com.bignerdranch.android.reciper.Database.SnapDbSchema.*;

/**
 *  A singleton class for managing recipes
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/22/15.
 */
public class RecipeBook {
    private static Context mContext;
    private static RecipeBook theRecipeBook;
    private static SQLiteDatabase mDatabase;

    /**
     * Constructor for RecipeBook
     * @param context application context
     */
    private RecipeBook(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Get the single instance of this class, create new if null
     * @param context applicaiton context
     * @return RecipeBook instance
     */
    public static RecipeBook getTheRecipeBook(Context context) {
        if (theRecipeBook == null) {
            theRecipeBook = new RecipeBook(context);
        }
        return theRecipeBook;
    }

    /**
     * Add a text to an existing commnet
     * @param text text to be added
     * @param comment comment to add the text too
     */
    public void addCommentText(String text, Comment comment) {
        comment.addTextComment(text);
        updateComment(comment);
    }

    /**
     * Add a new recipe to database
     * @param r new recipe
     */
    public void addRecipe(Recipe r) {
        ContentValues values = getContentValues(r);
        mDatabase.insert(RecipeTable.NAME, null, values);
    }

    /**
     * Delete a recipe
     * @param r recipe to be removed
     */
    public void deleteRecipes(Recipe r) {
        String uuidString = r.getID().toString();
        mDatabase.delete(RecipeTable.NAME, RecipeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Update a recipe
     * @param recipe recipe to be updated
     */
    public void updateRecipe(Recipe recipe) {
        String uuidString = recipe.getID().toString();
        ContentValues values = getContentValues(recipe);

        mDatabase.update(RecipeTable.NAME, values,
                RecipeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Add a new snap to databse
     * @param s new snap
     */
    public void addSnap(Snap s) {
        ContentValues values = getContentValues(s);
        mDatabase.insert(SnapTable.NAME, null, values);
    }

    /**
     * Delete a snap
     * @param s snap to be removed
     */
    public void deleteSnap(Snap s) {
        String uuidString = s.getId().toString();
        mDatabase.delete(SnapTable.NAME, SnapTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Update a snap
     * @param snap snap to be updated
     */
    public void updateSnap(Snap snap) {
        String uuidString = snap.getId().toString();
        ContentValues values = getContentValues(snap);

        mDatabase.update(SnapTable.NAME, values,
                SnapTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Add a new comment to database
     * @param c new comment
     */
    public void addComment(Comment c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CommentTable.NAME, null, values);
    }

    /**
     * Delete a commnet
     * @param c comment to be removed
     */
    public void deleteSnap(Comment c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CommentTable.NAME, CommentTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Update a commnet
     * @param comment commnet to be update
     */
    public void updateComment(Comment comment) {
        String uuidString = comment.getId().toString();
        ContentValues values = getContentValues(comment);

        mDatabase.update(CommentTable.NAME, values,
                CommentTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    /**
     * Get a recipe from databse
     * @param id recipe ID
     * @return recipe
     */
    public Recipe getRecipe(UUID id) {
        RecipeCursorWrapper cursor = queryRecipes(
                RecipeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }
    }

    /**
     * Get all recipes
     * @return list of all recipes
     */
    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        RecipeCursorWrapper cursor = queryRecipes(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                recipes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return recipes;
    }

    /**
     * Get a snap
     * @param snapId snap ID
     * @return snap
     */
    public Snap getSnap(UUID snapId) {
        RecipeCursorWrapper cursor = querySnaps(
                SnapTable.Cols.UUID + " = ?",
                new String[]{snapId.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSnap();
        } finally {
            cursor.close();
        }
    }

    /**
     * Get all snaps of a recipe
     * @param recipeId recipe ID
     * @return list of snaps
     */
    public ArrayList<Snap> getSnaps(UUID recipeId) {
        ArrayList<Snap> snaps = new ArrayList<>();

        RecipeCursorWrapper cursor = querySnaps(
                SnapTable.Cols.PARENT_RECIPE + " = ?",
                new String[]{recipeId.toString()});

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Snap snap = cursor.getSnap();
                snap.setComments(getComments(snap.getId()));
                snaps.add(0,snap);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return snaps;
    }

    /**
     * Get a comment
     * @param commentId comment ID
     * @return comment
     */
    public Comment getComment(UUID commentId) {
        RecipeCursorWrapper cursor = queryComments(
                CommentTable.Cols.UUID + " = ?",
                new String[]{commentId.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getComment();
        } finally {
            cursor.close();
        }
    }

    /**
     * Get all comments of a snap
     * @param snapId snap ID
     * @return list of comments
     */
    public ArrayList<Comment> getComments(UUID snapId) {
        ArrayList<Comment> comments = new ArrayList<>();

        RecipeCursorWrapper cursor = queryComments(
                CommentTable.Cols.PARENT_SNAP + " = ?",
                new String[]{snapId.toString()});

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                comments.add(cursor.getComment());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return comments;
    }


    /**
     * Get photo file of a snap object
     * @param snap
     * @return photo file
     */
    public File getPhotoFile(Snap snap) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, snap.getPictureFileName());
    }

    /**
     * Gets content values of a recipe
     * @param recipe
     * @return content values
     */
    private static ContentValues getContentValues(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.UUID, recipe.getID().toString());
        values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeTable.Cols.DATE, recipe.getDate().getTime());
        values.put(RecipeTable.Cols.CATEGORY, recipe.getCategory());
        values.put(RecipeTable.Cols.SERVINGS, recipe.getServings());
        values.put(RecipeTable.Cols.TAGS, recipe.getTags());
        values.put(RecipeTable.Cols.DURATION, recipe.getDuration());
        values.put(RecipeTable.Cols.DIFFICULTY, recipe.getDifficulty());
        values.put(RecipeTable.Cols.PRIMARY_SNAP, recipe.getPrimarySnap().toString());

        return values;
    }

    /**
     * Get content values of a snap
     * @param snap
     * @return content values
     */
    private static ContentValues getContentValues(Snap snap) {
        ContentValues values = new ContentValues();
        values.put(SnapTable.Cols.UUID, snap.getId().toString());
        values.put(SnapTable.Cols.PARENT_RECIPE, snap.getParentId().toString());
        values.put(SnapTable.Cols.DATE, snap.getDate().getTime());
        return values;
    }

    /**
     * Get content values of a comment
     * @param comment
     * @return content values
     */
    private static ContentValues getContentValues(Comment comment) {
        ContentValues values = new ContentValues();
        values.put(CommentTable.Cols.UUID, comment.getId().toString());
        values.put(CommentTable.Cols.PARENT_SNAP, comment.getParentId().toString());
        values.put(CommentTable.Cols.DATE, comment.getDate().getTime());
        values.put(CommentTable.Cols.COMMENT, comment.getCommentsText());
        values.put(CommentTable.Cols.X_COORD, comment.getX());
        values.put(CommentTable.Cols.Y_COORD, comment.getY());

        return values;
    }

    /**
     * Get cursor for querying recipe databse table
     * @param whereClause where clause of query
     * @param whereArgs arguments for query
     * @return cursor wrapper object
     */
    private RecipeCursorWrapper queryRecipes (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null, //all column
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                RecipeTable.Cols.DATE + " DESC" //order by
        );
        return new RecipeCursorWrapper(cursor);
    }

    /**
     * Get cursor for querying snaps databse table
     * @param whereClause where clause of query
     * @param whereArgs arguments for query
     * @return cursor wrapper object
     */
    private RecipeCursorWrapper querySnaps (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SnapTable.NAME,
                null, //all column
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null //order by
        );
        return new RecipeCursorWrapper(cursor);
    }

    /**
     * Get cursor for querying comments databse table
     * @param whereClause where clause of query
     * @param whereArgs arguments for query
     * @return cursor wrapper object
     */
    private RecipeCursorWrapper queryComments (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CommentTable.NAME,
                null, //all column
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null //order by
        );
        return new RecipeCursorWrapper(cursor);
    }
}
