package com.bignerdranch.android.reciper.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 *  A comment model
 *
 *  @author Basileal Imana, Bemnet Demere and Maria Dyane
 *  @version 1.0
 *  @since 11/12/15.
 */
public class Comment {

    // instance variables
    private UUID mID;
    private Date mDate;
    private UUID mParentSnapID;
    private float x;
    private float y;
    private String commentsText = ""; //combined string of all comments strings of this object

    /**
     * Default constructor
     */
    public Comment(){
        this(UUID.randomUUID());
    }

    /**
     * Constructor with ID
     * @param id
     */
    public Comment(UUID id) {
        mID = id;
        mDate = new Date();
    }

    // Getter and setter methods for instance variables
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

    /**
     * Adds a string text to an existing comment object
     * @param text
     */
    public void addTextComment(String text) {
        // if first time, no need to encode
        if(commentsText.length() == 0) {
            commentsText = text;
        }
        else {
            // get a list of all comment strings entered so far
            ArrayList<String> list = getCommentsList();
            // add the new comment to the list
            list.add(text);
            // encode all the comments into a single string
            setCommentsText(encode(list));
        }
    }

    /**
     * Edit a specific comment with the long list of comment strings
     * @param position
     * @param text
     */
    public void editTextComment(int position, String text) {
        // decode the string into a list
        ArrayList<String> list = getCommentsList();
        // update the text at specific postion in the list
        list.set(position,text);
        // encode back to one long string
        setCommentsText(encode(list));
    }

    public ArrayList<String> getCommentsList(){
        return decode(commentsText);
    }

    /**
     * Encodes all comment strings in a list into one big string
     * @param comments list of comment strings
     * @return encoded string
     */
    public static String encode(ArrayList<String> comments) {
        String encoded = comments.get(0);
        for(int i = 1; i < comments.size(); i++) {
            // add a '`' in between strings when encoding
            encoded = encoded + "`" + comments.get(i);
        }
        return encoded;
    }

    /**
     * Decodes the one long string into a list of string comments
     * @param textComment
     * @return
     */
    public static ArrayList<String> decode(String textComment) {
        // split string based on where the '`' character is
        String[] decoded = textComment.split("`");
        // convert to a array list
        ArrayList<String> list = new ArrayList(Arrays.asList(decoded));
        return list;
    }

}
