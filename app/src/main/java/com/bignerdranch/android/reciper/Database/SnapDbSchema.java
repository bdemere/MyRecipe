package com.bignerdranch.android.reciper.Database;

/**
 *  Defines a schema for snaps table
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 10/30/15.
 */
public class SnapDbSchema {
    public static final class SnapTable {
        // table name
        public static final String NAME = "snaps";

        // table columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARENT_RECIPE = "parent_recipe";
            public static final String DATE = "date";
        }
    }
}

