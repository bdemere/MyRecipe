package com.bignerdranch.android.reciper.data;

import android.content.Context;

import com.bignerdranch.android.reciper.data.Comment;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/12/15.
 */

public class Snap {
    private Context mContext;
    private UUID mID;
    private int mPicture;
    private UUID mParentRecipeID;
    private File mPhotoFile;
    private String mPictureFileName;

    private ArrayList<Comment> mComments;

    public ArrayList<Comment> getComments() {
        return mComments;
    }
    public Snap(UUID RecipeID){
        mID = UUID.randomUUID();
        mParentRecipeID = RecipeID;
        mComments = new ArrayList<Comment>();
        //CreateFile();
    }

    public UUID getID() {
        return mID;
    }
    public int getPicture() {
        return mPicture;
    }

    public String getPictureFileName(){
        String toReturn = "IMG_"
                + mID.toString()
                + "_" + mParentRecipeID.toString() + ".jpg";
        return toReturn;
    }


    public File getPhotoFile() {
        return mPhotoFile;
    }

    public void setPicture(int mPicture) {
        this.mPicture = mPicture;
    }

}
