package com.bignerdranch.android.reciper;

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
    private Snap mLatest;
    private ArrayList<Snap> mSnaps = new ArrayList<>();

    public Recipe(String title){
        ID = UUID.randomUUID();
        mRecipeTitle = title;
        Snap dummySnap = new Snap(ID);
        mSnaps.add(dummySnap);
        //testImageSetter();
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public String getTitle() {
        return mRecipeTitle;
    }

    public void testImageSetter(){
        Snap temp1 = new Snap(ID);
        Snap temp2 = new Snap(ID);
        Snap temp3 = new Snap(ID);
        temp1.setPicture(R.drawable.burger);
        temp2.setPicture(R.drawable.bbq);
        temp3.setPicture(R.drawable.download);
        mSnaps.add(mSnaps.size() - 1, temp1);
        mSnaps.add(mSnaps.size() - 1, temp2);
        mSnaps.add(mSnaps.size() - 1, temp3);
    }
    public UUID getID() {
        return ID;
    }

    public Snap getLatest(){
        return mLatest;
    }
    public ArrayList<Snap> getSnaps() {
        return mSnaps;
    }

    public Snap newSnap(){
        Snap newSnap = new Snap(ID);
        mSnaps.add((mSnaps.size() - 1),newSnap);
        newSnap.setPicture(R.drawable.burger); // temporary
        mLatest = newSnap;
        return newSnap;
    }
}
