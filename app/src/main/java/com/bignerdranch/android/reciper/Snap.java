package com.bignerdranch.android.reciper;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bubujay on 11/12/15.
 */

public class Snap {
    private Context mContext;
    private int mID;
    private int mPicture;
    private UUID mParentRecipeID;
    private File mPhotoFile;
    private String mPictureFileName;

    private ArrayList<Comment> mComments;

    public ArrayList<Comment> getComments() {
        return mComments;
    }
    public Snap(UUID RecipeID){
        mParentRecipeID = RecipeID;
        mComments = new ArrayList<Comment>();
        //CreateFile();
    }

    public int getID() {
        return mID;
    }
    public int getPicture() {
        return mPicture;
    }

    public String getPictureFileName(){
        String toReturn = "IMG_"
                + mParentRecipeID.toString()
                + " " + mID + ".jpg";
        return toReturn;
    }

    private File CreateFile(){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null) {
            return null;
        }
        mPhotoFile =  new File(externalFilesDir, getPictureFileName());
        return mPhotoFile;
    }

    public File getPhotoFile() {
        return mPhotoFile;
    }

    public void setPicture(int mPicture) {
        this.mPicture = mPicture;
    }

}
