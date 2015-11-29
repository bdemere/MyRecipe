package com.bignerdranch.android.reciper.Database;

/**
 * Created by bimana2 on 11/28/15.
 */
public class CommentDbSchema {
    public static final class CommentTable {
        public static final String NAME = "comments";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARENT_SNAP = "parent_snap";
            public static final String DATE = "date";
            public static final String COMMENT = "comment";
        }
    }
}
