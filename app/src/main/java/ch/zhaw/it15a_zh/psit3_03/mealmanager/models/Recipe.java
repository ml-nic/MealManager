package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This is the domain class for a recipe.
 */
public class Recipe {
    private int recipeID;
    private String name;
    private String description;
    private String preparation;
    private String image;
    private int cookingTime;
    private double healthRating;
    private int favorite;
    private int servings;
    private String type;
    private ArrayList<RecipeItem> recipeItems;
    private String course;

    public Recipe(int recipeID, String name, String description, String preparation, String image, int cookingTime,
                  double healthRating, int favorite, int servings, String type, ArrayList<RecipeItem> recipeItems, String course) {
        this.recipeID = recipeID;
        this.name = name;
        this.description = description;
        this.preparation = preparation;
        this.image = image;
        this.cookingTime = cookingTime;
        this.healthRating = healthRating;
        this.favorite = favorite;
        this.servings = servings;
        this.type = type;
        this.recipeItems = recipeItems;
        this.course = course;
    }

    public boolean isTypeOf(String filterPattern) {
        String[] types = type.split(";");
        for (String tempType : types) {
            if (tempType.equals(filterPattern)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCourseOf(String filterPattern) {
        String[] courses = course.split(";");
        for (String tempCourse : courses) {
            if (tempCourse.equals(filterPattern)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(ArrayList<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public double getHealthRating() {
        return healthRating;
    }

    public void setHealthRating(double healthRating) {
        this.healthRating = healthRating;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) object;
        return (this.getRecipeID() == other.getRecipeID() && this.getName().equals(other.getName()));
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Recipe{" + "recipeID=" + recipeID + ", name='" + name + '\'' + ", description='" + description + '\'' +
                ", preparation='" + preparation + '\'' + ", image='" + image + '\'' + ", cookingTime=" + cookingTime +
                ", healthRating=" + healthRating + ", favorite=" + favorite + ", servings=" + servings + ", type='" + type +
                '\'' + ", recipeItems=" + recipeItems + ", course='" + course + '\'' + '}';
    }


    /**
     * Comparator to compare Recipes by name ascending.
     */
    public static final Comparator<Recipe> sortByNameAscending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            String name1 = recipe1.name;
            String name2 = recipe2.name;
            return name1.compareTo(name2);
        }
    };
    /**
     * Comparator to compare Recipes by name descending.
     */
    public static final Comparator<Recipe> sortByNameDescending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            String name1 = recipe1.name;
            String name2 = recipe2.name;
            return name2.compareTo(name1);
        }
    };
    /**
     * Comparator to compare Recipes by healthrating ascending.
     */
    public static final Comparator<Recipe> sortByHealthratingAscending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            Double healthrating1 = recipe1.healthRating;
            Double healthrating2 = recipe2.healthRating;
            return healthrating1.compareTo(healthrating2);
        }
    };
    /**
     * Comparator to compare Recipes by healthrating descending.
     */
    public static final Comparator<Recipe> sortByHealthratingDescending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            Double healthrating1 = recipe1.healthRating;
            Double healthrating2 = recipe2.healthRating;
            return healthrating2.compareTo(healthrating1);
        }
    };
    /**
     * Comparator to compare Recipes by cookingtime ascending.
     */
    public static final Comparator<Recipe> sortByCookingtimeAscending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            Integer cookingtime1 = recipe1.cookingTime;
            Integer cookingtime2 = recipe2.cookingTime;
            return cookingtime1.compareTo(cookingtime2);
        }
    };
    /**
     * Comparator to compare Recipes by cookingtime descending.
     */
    public static final Comparator<Recipe> sortByCookingtimeDescending = new Comparator<Recipe>() {
        public int compare(Recipe recipe1, Recipe recipe2) {
            Integer cookingtime1 = recipe1.cookingTime;
            Integer cookingtime2 = recipe2.cookingTime;
            return cookingtime2.compareTo(cookingtime1);
        }
    };

}