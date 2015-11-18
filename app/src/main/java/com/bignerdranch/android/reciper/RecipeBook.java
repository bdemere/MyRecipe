package com.bignerdranch.android.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bubujay on 11/14/15.
 */
public class RecipeBook {
    private static RecipeBook theRecipeBook;

    private ArrayList<Recipe> theRecipes = new ArrayList<>();

    public RecipeBook(){
        testRecipeSetter();
        testRecipeSetter();
        testRecipeSetter();
        testRecipeSetter();
    }
    public void testRecipeSetter(){
        Recipe recipe1 = new Recipe("Burger1");
        Recipe recipe2 = new Recipe("Burger2");
        Recipe recipe3 = new Recipe("Burger3");
        theRecipes.add(recipe1);
        theRecipes.add(recipe2);
        theRecipes.add(recipe3);
    }
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
