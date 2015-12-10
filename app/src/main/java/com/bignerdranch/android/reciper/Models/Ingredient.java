package com.bignerdranch.android.reciper.Models;

import java.util.Date;
import java.util.UUID;

/**
 * Created by bubujay on 12/9/15.
 */
public class Ingredient {
    private UUID mID;
    private Date mDate;
    private UUID mParentSnapID;
    private float x;
    private float y;
    private String mIngredient;
    private float amount;

    public Ingredient(){
        this(UUID.randomUUID());
    }

    public Ingredient(UUID id){
        mID = id;
        mDate = new Date();
    }

    public void setIngredient(String ingredient){
        mIngredient = ingredient;
    }

    public String getIngredient(){
        return mIngredient;
    }

    public void setCoordinate(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
