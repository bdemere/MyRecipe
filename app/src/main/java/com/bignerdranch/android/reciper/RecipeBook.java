package com.bignerdranch.android.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bubujay on 11/14/15.
 */
public class RecipeBook {
    private static RecipeBook theRecipeBook;

    private ArrayList<Recipe> theRecipes;

    public static RecipeBook getTheRecipeBook() {
        if (theRecipeBook == null) {
            theRecipeBook = new RecipeBook();
        }
        return theRecipeBook;
    }

    public List<Recipe> getTheRecipes() {
        return theRecipes;
    }
}
