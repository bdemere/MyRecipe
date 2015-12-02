package com.bignerdranch.android.reciper.Models;

import com.bignerdranch.android.reciper.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class Recipe {
    private String mRecipeTitle;
    private Date mDate;
    private UUID ID;
    private ArrayList<Snap> mSnaps = new ArrayList<>();

    public Recipe(){
        this(UUID.randomUUID());
    }

    public Recipe(UUID id) {
        ID = id;
        Snap dummySnap = new Snap(ID);
        mSnaps.add(dummySnap);
        mDate = new Date();
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

    public static Snap newSnap(UUID recipeID){
        Snap newSnap = new Snap();
        newSnap.setmParentRecipeID(recipeID);
        //mSnaps.add(0,newSnap);
        //newSnap.setPicture(R.drawable.burger); // temporary
        return newSnap;
    }
}
