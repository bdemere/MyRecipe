package com.bignerdranch.android.reciper;

import java.util.List;

/**
 * Created by bubujay on 11/12/15.
 */

public class Snap {
    private int mID;
    private int mPicture;
    private List<Comment> mComments;

    public List<Comment> getmComments() {
        return mComments;
    }

    public void setmComments(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getPicture() {
        return mPicture;
    }

    public void setPicture(int mPicture) {
        this.mPicture = mPicture;
    }
}
