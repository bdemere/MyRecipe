package com.bignerdranch.android.reciper.Models;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
    private Date mDate;

    private ArrayList<Comment> mComments;

    public Snap(){
        this(UUID.randomUUID());
    }

    public Snap(UUID id) {
        mID = id;
        mComments = new ArrayList<>();
        mDate = new Date();
    }

    public void setmParentRecipeID (UUID parentId) {
        mParentRecipeID = parentId;
    }
    public ArrayList<Comment> getComments() {
        return mComments;
    }

    public static Comment newComment(UUID snapId){
        Comment comment = new Comment();
        comment.setParentSnapID(snapId);
        return comment;
    }

    public Comment getLatestComment(){
        return mComments.get(mComments.size() - 1);
    }

    public Comment searchComments(float x, float y){
        int side = 260;
        Log.d("TAG", "Number of comments: " + mComments.size());
        for(Comment comment : mComments){
            if((int)comment.getX() / side == (int)x / side
                    && (int)comment.getY() / side == (int)y / side)
                return comment;
            /*Log.d("TAG", "Diff X: " + Math.abs((int)comment.getX() - (int)x));
            Log.d("TAG", "Diff Y: " + Math.abs((int)comment.getY() - (int)y));
            if((Math.abs((int)comment.getX() - (int)x) < side)
                    && (Math.abs((int)comment.getY() - (int)y) < side))
                return comment;*/
        }
        return null;
    }

    public void setComments(ArrayList<Comment> commentsList) {
        mComments = commentsList;
    }

    public boolean hasComments(){
        return !mComments.isEmpty();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getParentId() {
        return mParentRecipeID;
    }


    public UUID getId() {
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
    /*public File getPhotoFile() {
        return mPhotoFile;
    }*/
    public void setPicture(int mPicture) {
        this.mPicture = mPicture;
    }

    //Temporary

    private ArrayList<Ingredient> mIngredients = new ArrayList<>();

    public void addIngredient(Ingredient i){
        mIngredients.add(i);
    }

    public ArrayList<Ingredient> getIngredientList(){
        return mIngredients;
    }


}
