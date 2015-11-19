package com.bignerdranch.android.reciper;

import java.util.List;

/**
 * Created by bubujay on 11/12/15.
 */

public class Snap {
    private int mID;
    private int mPicture;
    private List<Comment> mComments;

    public List<Comment> getComments() {
        return mComments;
    }

    public void setComments(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public int getID() {
        return mID;
    }
    public int getPicture() {
        return mPicture;
    }

    public void setPicture(int mPicture) {
        this.mPicture = mPicture;
    }
}
