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
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.reciper.Database.CommentDbSchema.*;
import static com.bignerdranch.android.reciper.Database.RecipeDbSchema.*;
import static com.bignerdranch.android.reciper.Database.SnapDbSchema.*;

/**
 * Created by bubujay on 11/14/15.
 */
public class RecipeBook {
    private static Context mContext;
    private static RecipeBook theRecipeBook;
    private static SQLiteDatabase mDatabase;

    private RecipeBook(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public static RecipeBook getTheRecipeBook(Context context) {
        if (theRecipeBook == null) {
            theRecipeBook = new RecipeBook(context);
        }
        return theRecipeBook;
    }

    public void addCommentText(String text, Comment comment) {
        comment.addTextComment(text);
        updateComment(comment);
    }

    public void addRecipe(Recipe r) {
        ContentValues values = getContentValues(r);
        mDatabase.insert(RecipeTable.NAME, null, values);
    }

    public void deleteRecipes(Recipe r) {
        String uuidString = r.getID().toString();
        mDatabase.delete(RecipeTable.NAME, RecipeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateRecipe(Recipe recipe) {
        String uuidString = recipe.getID().toString();
        ContentValues values = getContentValues(recipe);

        mDatabase.update(RecipeTable.NAME, values,
                RecipeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void addSnap(Snap s) {
        ContentValues values = getContentValues(s);
        mDatabase.insert(SnapTable.NAME, null, values);
    }

    public void deleteSnap(Snap s) {
        String uuidString = s.getId().toString();
        mDatabase.delete(SnapTable.NAME, SnapTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }
    public void updateSnap(Snap snap) {
        String uuidString = snap.getId().toString();
        ContentValues values = getContentValues(snap);

        mDatabase.update(SnapTable.NAME, values,
                SnapTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void addComment(Comment c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CommentTable.NAME, null, values);
    }

    public void deleteSnap(Comment c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CommentTable.NAME, CommentTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateComment(Comment comment) {
        String uuidString = comment.getId().toString();
        ContentValues values = getContentValues(comment);

        mDatabase.update(CommentTable.NAME, values,
                CommentTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

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

    /*public Recipe getRecipe(UUID ID){
        for(Recipe recipe: theRecipes){
            if(recipe.getID().equals(ID)) {
                return recipe;
            }
        }
        return null;
    }*/

    public File getPhotoFile(Snap snap) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, snap.getPictureFileName());
    }

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

        return values;
    }

    private static ContentValues getContentValues(Snap snap) {
        ContentValues values = new ContentValues();
        values.put(SnapTable.Cols.UUID, snap.getId().toString());
        values.put(SnapTable.Cols.PARENT_RECIPE, snap.getParentId().toString());
        values.put(SnapTable.Cols.DATE, snap.getDate().getTime());
        return values;
    }

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

    private RecipeCursorWrapper queryRecipes (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null, //all column
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null //order by
        );
        return new RecipeCursorWrapper(cursor);
    }

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
