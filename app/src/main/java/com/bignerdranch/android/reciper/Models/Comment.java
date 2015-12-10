package com.bignerdranch.android.reciper.Models;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String commentsText = ""; //combined string of all comments strings of this object

    public Comment(){
        this(UUID.randomUUID());
    }

    public Comment(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public String getCommentsText() {
        return commentsText;
    }

    public void setCommentsText(String text) {
        commentsText = text;
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

    public void addTextComment(String text) {
        ArrayList<String> list = getCommentsList();
        list.add(text);
        setCommentsText(encode(list));
    }

    public void editTextComment(int position, String text) {
        ArrayList<String> list = getCommentsList();
        list.set(position,text);
        setCommentsText(encode(list));
    }

    public ArrayList<String> getCommentsList(){
        return decode(commentsText);
    }

    public static String encode(ArrayList<String> comments) {
        String encoded = comments.get(0);
        for(int i = 1; i < comments.size(); i++) {
            encoded = encoded + "`" + comments.get(i);
        }
        return encoded;
    }

    public static ArrayList<String> decode(String textComment) {
        String[] decoded = textComment.split("`");
        return new ArrayList(Arrays.asList(decoded));
    }

}
