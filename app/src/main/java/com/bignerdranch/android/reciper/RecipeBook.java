package com.bignerdranch.android.reciper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bubujay on 11/14/15.
 */
public class RecipeBook {
    private static Context mContext;
    private static RecipeBook theRecipeBook;
    private static int mLatestRecipe = -1;

    private ArrayList<Recipe> theRecipes = new ArrayList<>();

    private RecipeBook(Context context){
        mContext = context.getApplicationContext();
        //testRecipeSetter();
        //testRecipeSetter();
        //testRecipeSetter();
        //testRecipeSetter();
    }
    public void testRecipeSetter(){
        Recipe recipe1 = new Recipe("Burger1");
        Recipe recipe2 = new Recipe("Burger2");
        Recipe recipe3 = new Recipe("Burger3");
        theRecipes.add(recipe1);
        theRecipes.add(recipe2);
        theRecipes.add(recipe3);
    }
    public static RecipeBook getTheRecipeBook(Context context) {
        if (theRecipeBook == null) {
            theRecipeBook = new RecipeBook(context);
        }
        return theRecipeBook;
    }

    public Recipe getRecipe(UUID ID){
        for(Recipe recipe: theRecipes){
            if(recipe.getID().equals(ID)) {
                return recipe;
            }
        }
        return null;
    }
    public Recipe getLatest(){
        if(mLatestRecipe == -1)
            return null;
        return theRecipes.get(mLatestRecipe);
    }
    public List<Recipe> getTheRecipes() {
        return theRecipes;
    }
    public Recipe newRecipe(String name){
        Recipe newRecipe = new Recipe(name);
        theRecipes.add(newRecipe);
        mLatestRecipe++;
        return newRecipe;
    }

    public File getPhotoFile(Snap snap) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, snap.getPictureFileName());
    }
}
