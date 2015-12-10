package com.bignerdranch.android.reciper.Database;

/**
 * Created by remember on 10/30/2015.
 */
public class SnapDbSchema {
    public static final class SnapTable {
        public static final String NAME = "snaps";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARENT_RECIPE = "parent_recipe";
            public static final String DATE = "date";
        }
    }
}

