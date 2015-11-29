package com.bignerdranch.android.reciper.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bubujay on 11/12/15.
 */
public class Comment {
    private UUID mID;
    private Date mDate;
    private UUID mParentSnapID;
    private float x;
    private float y;
    private String textComment;
    private ArrayList<String> textComments;

    public Comment(){
        this(UUID.randomUUID());
    }

    public Comment(UUID id) {
        mID = id;
        textComments = new ArrayList<>();
        mDate = new Date();
    }

    public String getCommentText() {
        return textComment;
    }

    public void setCommentText(String text) {
        textComment = text;
    }

    public void setParentSnapID (UUID parentId) {
        mParentSnapID = parentId;
    }

    public UUID getId() {
        return mID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getParentId() {
        return mParentSnapID;
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
