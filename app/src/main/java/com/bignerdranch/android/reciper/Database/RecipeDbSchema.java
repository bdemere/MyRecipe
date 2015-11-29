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
        }
    }
}
