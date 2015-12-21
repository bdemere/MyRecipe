package com.bignerdranch.android.reciper.Database;

/**
 *  Defines a schema for recipes table
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 10/30/15.
 */
public class RecipeDbSchema {
    public static final class RecipeTable {
        // table name
        public static final String NAME = "recipes";

        // table columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String CATEGORY = "category";
            public static final String SERVINGS = "servings";
            public static final String TAGS = "tags";
            public static final String DURATION = "duration";
            public static final String DIFFICULTY = "difficulty";
            public static final String PRIMARY_SNAP = "snapid";
        }
    }
}
