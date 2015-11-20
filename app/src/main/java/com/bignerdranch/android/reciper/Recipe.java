package com.bignerdranch.android.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bubujay on 11/14/15.
 */
public class Recipe {
    private static Recipe thisRecipe;
    private int ID;
    private ArrayList<Snap> mSnaps = new ArrayList<>();

    public static Recipe getThisRecipe(){
        if(thisRecipe == null) {
            thisRecipe = new Recipe();
        }
        return thisRecipe;
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
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Snap> getSnaps() {
        return mSnaps;
    }
}
