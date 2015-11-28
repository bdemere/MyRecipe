package com.bignerdranch.android.reciper.data;

import java.util.ArrayList;

/**
 * Created by bubujay on 11/12/15.
 */
public class Comment {
    private float x;
    private float y;
    private String textComment;
    private ArrayList<String> textComments;

    public Comment(){
        textComments = new ArrayList<>();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public void setTextComment(String text){
        textComment = text;
    }
    public void addTextComment(String text) {
        textComments.add(text);
    }
    public String getTextComment(){
        return textComment;
    }
    public ArrayList<String> getTextComments(){
        return textComments;
    }



}
