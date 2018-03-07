package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

import java.util.ArrayList;

/**
 * Builder for an recipe, each field is optional.
 */
public class RecipeBuilder {
    private int recipeID = -1;
    private String name = "";
    private String description = "";
    private String preparation = "";
    private String image = "";
    private int cookingTime = -1;
    private double healthRating = -1.0;
    private int favorite = 0;
    private int servings = 0;
    private String type = "";
    private String course = "";
    private ArrayList<RecipeItem> recipeItems = null;

    public RecipeBuilder setRecipeID(int recipeID) {
        this.recipeID = recipeID;
        return this;
    }

    public RecipeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RecipeBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public RecipeBuilder setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public RecipeBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public RecipeBuilder setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeBuilder setHealthRating(double healthRating) {
        this.healthRating = healthRating;
        return this;
    }

    public RecipeBuilder setFavorite(int favorite) {
        this.favorite = favorite;
        return this;
    }

    public RecipeBuilder setServings(int servings) {
        this.servings = servings;
        return this;
    }

    public RecipeBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public RecipeBuilder setCourse(String course) {
        this.course = course;
        return this;
    }

    public RecipeBuilder setRecipeItems(ArrayList<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
        return this;
    }

    /**
     * Creates an recipe out of the RecipeBuilder.
     *
     * @return The new recipe.
     */
    public Recipe createRecipe() {
        return new Recipe(recipeID, name, description, preparation, image, cookingTime, healthRating, favorite, servings,
                type, recipeItems, course);
    }
}