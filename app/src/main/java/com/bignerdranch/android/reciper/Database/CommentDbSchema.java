package com.bignerdranch.android.reciper.Database;

/**
 *  Defines a schema for comments table
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 10/30/15.
 */
public class CommentDbSchema {
    public static final class CommentTable {
        // table name
        public static final String NAME = "comments";

        // table columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARENT_SNAP = "parent_snap";
            public static final String DATE = "date";
            public static final String COMMENT = "comment";
            public static final String X_COORD = "x_coordinate";
            public static final String Y_COORD = "y_coordinate";
        }
    }
}
