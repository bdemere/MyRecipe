package com.bignerdranch.android.reciper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public Recipe getRecipe(UUID ID){
        for(Recipe recipe: theRecipes){
            if(recipe.getID() == ID)
                return recipe;
        }
        return null;
    }
    public List<Recipe> getTheRecipes() {
        return theRecipes;
    }
}
