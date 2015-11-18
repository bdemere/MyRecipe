package com.bignerdranch.android.reciper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class Recipe {
    private static Recipe thisRecipe;
    private String mRecipeTitle;
    private Date mDate;
    private UUID ID;
    private ArrayList<Snap> mSnaps = new ArrayList<>();

    public Recipe(String title){
        mRecipeTitle = title;
        testImageSetter();
        mDate = new Date();
    }

    public static Recipe getThisRecipe(){
        return thisRecipe;
    }


    public Date getDate() {
        return mDate;
    }

    public String getTitle() {
        return mRecipeTitle;
    }

    public void setTitle(String mRecipeTitle) {
        this.mRecipeTitle = mRecipeTitle;
    }
    public void testImageSetter(){
        Snap temp1 = new Snap();
        Snap temp2 = new Snap();
        Snap temp3 = new Snap();
        temp1.setPicture(R.drawable.burger);
        temp2.setPicture(R.drawable.bbq);
        temp3.setPicture(R.drawable.download);
        mSnaps.add(temp1);
        mSnaps.add(temp2);
        mSnaps.add(temp3);
    }
    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public List<Snap> getSnaps() {
        return mSnaps;
    }
}
