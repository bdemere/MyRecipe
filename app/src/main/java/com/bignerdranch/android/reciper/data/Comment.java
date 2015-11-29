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
    private ArrayList<String> mAudioFileName;
    public static int audioFilesCounter = 0;

    public Comment(){
        mAudioFileName = new ArrayList<>();
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

    public String getAudioFileName(){
        mAudioFileName.add("AUD_"
                + x
                +"_" + y + audioFilesCounter + ".3gp");
        audioFilesCounter++;
        return mAudioFileName.get(mAudioFileName.size() - 1);
    }



}
