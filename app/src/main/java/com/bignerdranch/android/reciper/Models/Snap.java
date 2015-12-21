package com.bignerdranch.android.reciper.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *  A snap model
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/12/15.
 */
public class Snap {

    // instance variables
    private UUID mID;
    private UUID mParentRecipeID;
    private Date mDate;
    private ArrayList<Comment> mComments;

    /**
     * Default constructor
     */
    public Snap(){
        this(UUID.randomUUID());
    }

    /**
     * Constructor with ID
     * @param id
     */
    public Snap(UUID id) {
        mID = id;
        mComments = new ArrayList<>();
        mDate = new Date();
    }

    // Getter and setter methods for instance variables
    public void setParentRecipeID(UUID parentId) {
        mParentRecipeID = parentId;
    }

    public ArrayList<Comment> getComments() {
        return mComments;
    }

    public void setComments(ArrayList<Comment> commentsList) {
        mComments = commentsList;
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

    /**
     * Given xy coordinates, searches for comment in near proximity
     * @param x x coordinate
     * @param y y coordinate
     * @return comment if found, null if not found
     */
    public Comment searchComments(float x, float y){
        int side = 260;
        for(Comment comment : mComments){
            if((int)comment.getX() / side == (int)x / side
                    && (int)comment.getY() / side == (int)y / side)
                return comment;
        }
        return null;
    }

    /**
     * Get image file name based on snap and recipe ID
     * @return snap's file name
     */
    public String getPictureFileName(){
        String toReturn = "IMG_"
                + mID.toString()
                + "_" + mParentRecipeID.toString() + ".jpg";
        return toReturn;
    }

    /**
     * Static method for creating a new comment given a parent snap ID
     * @param snapId parent snap ID
     * @return new comment
     */
    public static Comment newComment(UUID snapId){
        Comment comment = new Comment();
        comment.setParentSnapID(snapId);
        return comment;
    }

}
