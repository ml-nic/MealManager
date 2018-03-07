package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

/**
 * This is the domain class for UserPlannedRecipes. UserPlannedRecipes contain a recipeID(int), plannedServings(int)
 * and date on which the recipe is planned(String).
 */
public class UserPlannedRecipe {
  private int recipeID;
  private int plannedServings;
  private String datePlanned;
  
  public UserPlannedRecipe() {
  }
  
  public int getRecipeID() {
    return recipeID;
  }
  
  public void setRecipeID(int recipeID) {
    this.recipeID = recipeID;
  }
  
  public String getDatePlanned() {
    return datePlanned;
  }
  
  public void setDatePlanned(String datePlanned) {
    this.datePlanned = datePlanned;
  }
  
  public int getPlannedServings() {
    return plannedServings;
  }
  
  public void setPlannedServings(int plannedServings) {
    this.plannedServings = plannedServings;
  }
}
