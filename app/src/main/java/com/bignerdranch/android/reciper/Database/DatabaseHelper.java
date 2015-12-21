package com.bignerdranch.android.reciper.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.reciper.Database.CommentDbSchema.*;
import static com.bignerdranch.android.reciper.Database.RecipeDbSchema.RecipeTable;
import static com.bignerdranch.android.reciper.Database.SnapDbSchema.SnapTable;

/**
 *  Database helper class for creating and updating tables
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/28/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recipeBase.db";

    // table create statement for recipe table
    private static final String CREATE_TABLE_RECIPE =
            "create table " + RecipeTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    RecipeTable.Cols.UUID + ", " +
                    RecipeTable.Cols.TITLE + ", " +
                    RecipeTable.Cols.DATE + ", " +
                    RecipeTable.Cols.CATEGORY + ", " +
                    RecipeTable.Cols.SERVINGS + ", " +
                    RecipeTable.Cols.TAGS + ", " +
                    RecipeTable.Cols.DIFFICULTY + ", " +
                    RecipeTable.Cols.DURATION + ", " +
                    RecipeTable.Cols.PRIMARY_SNAP +
                    ")";

    // table create statement for snap table
    private static final String CREATE_TABLE_SNAP =
            "create table " + SnapTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    SnapTable.Cols.UUID + ", " +
                    SnapTable.Cols.PARENT_RECIPE + ", " +
                    SnapTable.Cols.DATE +
                    ")";

    // table create statement for comments table
    private static final String CREATE_TABLE_COMMENTS =
            "create table " + CommentTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    CommentTable.Cols.UUID + ", " +
                    CommentTable.Cols.PARENT_SNAP + ", " +
                    CommentTable.Cols.DATE + ", " +
                    CommentTable.Cols.COMMENT + ", " +
                    CommentTable.Cols.X_COORD + ", " +
                    CommentTable.Cols.Y_COORD +
                    ")";


    /**
     * Constructor for creating database
     * @param context application context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPE);
        db.execSQL(CREATE_TABLE_SNAP);
        db.execSQL(CREATE_TABLE_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}