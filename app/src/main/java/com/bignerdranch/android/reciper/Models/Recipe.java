package com.bignerdranch.android.reciper.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *  A recipe model
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/14/15.
 */
public class Recipe {

    // instance variables
    private String mRecipeTitle;
    private Date mDate;
    private UUID ID;
    private String mCategory;
    private String mServings;
    private String mTags;
    private long mDuration;
    private String mDifficulty;
    private ArrayList<Snap> mSnaps;
    private UUID mPrimarySnap;

    /**
     * Default constructor
     */
    public Recipe(){
        this(UUID.randomUUID());
    }

    /**
     * Constructor with ID
      * @param id
     */
    public Recipe(UUID id) {
        ID = id;
        mSnaps = new ArrayList<>();
        Snap dummySnap = new Snap(ID); //create a dummy snap
        mSnaps.add(dummySnap);
        mDate = new Date();
        mCategory = "Other";
        mServings = "N/A";
        mTags = "N/A";
        mDifficulty = "Intermediate";
        mPrimarySnap = dummySnap.getId(); //this feature is not functional as of now
    }

    // Getter and setter methods for instance variables
    public void setDifficulty(String difficulty) {
        this.mDifficulty = difficulty;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String mServings) {
        this.mServings = mServings;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String mTags) {
        this.mTags = mTags;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTitle(String title) {
        mRecipeTitle = title;
    }

    public String getTitle() {
        return mRecipeTitle;
    }

    public UUID getID() {
        return ID;
    }

    public ArrayList<Snap> getSnaps() {
        return mSnaps;
    }

    public UUID getSnapID(int position) {
        return mSnaps.get(position).getId();
    }

    public void setPrimarySnap(UUID snapId) {
        mPrimarySnap = snapId;
    }

    public UUID getPrimarySnap() {
        return mPrimarySnap;
    }

    /**
     * Static method for creating a new snap given a parent recipe ID
     * @param recipeID parent recipe ID
     * @return new snap
     */
    public static Snap newSnap(UUID recipeID){
        Snap newSnap = new Snap();
        newSnap.setParentRecipeID(recipeID);
        return newSnap;
    }
}
