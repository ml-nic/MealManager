package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

/**
 * Data Domain class for RecipeItems.
 */
public class RecipeItem {
    private int recipeItemID;
    private int recipeID;
    private int itemID;
    private String unit;
    private double amount;

    public RecipeItem() {
    }

    public RecipeItem(int recipeID, int itemID, String unit, double amount) {
        this.recipeID = recipeID;
        this.itemID = itemID;
        this.unit = unit;
        this.amount = amount;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getRecipeItemID() {
        return recipeItemID;
    }

    public void setRecipeItemID(int recipeItemID) {
        this.recipeItemID = recipeItemID;
    }

    /**
     * Compare two recipeItems.
     * If recipeItemID, recipeID and itemID are the same, then they are equal.
     *
     * @param object The other object of RecipeItem.
     * @return true, if they are equal, else false.
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof RecipeItem)) {
            return false;
        }
        RecipeItem other = (RecipeItem) object;
        return (this.getRecipeItemID() == other.getRecipeItemID() && this.getRecipeID() == other.getRecipeID() &&
                this.getItemID() == other.getItemID());
    }
}