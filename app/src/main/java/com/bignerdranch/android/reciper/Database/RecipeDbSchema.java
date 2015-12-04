package com.bignerdranch.android.reciper.Database;

/**
 * Created by remember on 10/30/2015.
 */
public class RecipeDbSchema {
    public static final class RecipeTable {
        public static final String NAME = "recipes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String CATEGORY = "category";
            public static final String SERVINGS = "servings";
            public static final String TAGS = "tags";
            public static final String DURATION = "duration";
            public static final String DIFFICULTY = "difficulty";
        }
    }
}
