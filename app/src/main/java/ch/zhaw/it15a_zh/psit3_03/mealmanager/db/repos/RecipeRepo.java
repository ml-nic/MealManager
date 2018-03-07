package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeBuilder;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.CrudRepository;

/**
 * Handles the specific SQL Statements of the recipe Table.
 * Every table has a own TableRepo to handle the specific SQL statments.
 */
public class RecipeRepo implements CrudRepository<Recipe> {
  public static final String TABLE_NAME = "recipe";
  private static final String RECIPEID = "recipeID";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String PREPARATION = "preparation";
  private static final String SERVINGS = "servings";
  private static final String COOKINGTIME = "cookingtime";
  private static final String IMAGEID = "imageID";
  private static final String HEALTHRATING = "healthrating";
  private static final String TYPE = "type";
  private static final String FAVORITE = "favourite";
  private static final String COURSE = "course";
  
  /**
   * Inserts a new recipe into the recipe table
   *
   * @param recipe Recipe object
   */
  @Override
  public Recipe insert(Recipe recipe) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    ContentValues values = new ContentValues();
    values.put(NAME, recipe.getName());
    values.put(DESCRIPTION, recipe.getDescription());
    values.put(PREPARATION, recipe.getPreparation());
    values.put(SERVINGS, recipe.getServings());
    values.put(HEALTHRATING, recipe.getHealthRating());
    values.put(IMAGEID, recipe.getImage());
    values.put(COOKINGTIME, recipe.getCookingTime());
    values.put(FAVORITE, recipe.getFavorite());
    values.put(TYPE, recipe.getType());
    values.put(COURSE, recipe.getCourse());
    int id = (int) db.insert(TABLE_NAME, null, values);
    DBManager.getInstance().closeDatabase();
    recipe.setRecipeID(id);
    return recipe;
  }
  
  /**
   * Gets all RecipeObjects in the database
   *
   * @return ArrayList<Recipe> recipes
   */
  @Override
  public ArrayList<Recipe> findAll() {
    String query = "SELECT * FROM " + TABLE_NAME;
    ArrayList<Recipe> recipes = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Cursor cursor = db.rawQuery(query, null);
    
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        Recipe recipe = new RecipeBuilder().setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)))
            .setName(cursor.getString(cursor.getColumnIndex(NAME)))
            .setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)))
            .setPreparation(cursor.getString(cursor.getColumnIndex(PREPARATION)))
            .setCookingTime(cursor.getInt(cursor.getColumnIndex(COOKINGTIME)))
            .setFavorite(cursor.getInt(cursor.getColumnIndex(FAVORITE)))
            .setHealthRating(cursor.getDouble(cursor.getColumnIndex(HEALTHRATING)))
            .setType(cursor.getString(cursor.getColumnIndex(TYPE)))
            .setImage(cursor.getString(cursor.getColumnIndex(IMAGEID)))
            .setCourse(cursor.getString(cursor.getColumnIndex(COURSE))).createRecipe();
        recipes.add(recipe);
        System.out.println(recipe);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return recipes;
  }
  
  /**
   * Updates a Recipe in the recipe Table
   *
   * @param recipe Recipe object which will overwrite the current recipe in the recipe table
   */
  @Override
  public int update(Recipe recipe) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    ContentValues values = new ContentValues();
    values.put(NAME, recipe.getName());
    values.put(DESCRIPTION, recipe.getDescription());
    values.put(PREPARATION, recipe.getPreparation());
    values.put(HEALTHRATING, recipe.getHealthRating());
    values.put(TYPE, recipe.getType());
    values.put(IMAGEID, recipe.getImage());
    values.put(COOKINGTIME, recipe.getCookingTime());
    values.put(FAVORITE, recipe.getFavorite());
    values.put(COURSE, recipe.getCourse());
    
    int result = db.update(TABLE_NAME, values, RECIPEID + "=?", new String[]{String.valueOf(recipe.getRecipeID())});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Deletes a recipe from the recipe table
   *
   * @param ID of the item
   */
  @Override
  public int delete(int ID) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    int result = db.delete(TABLE_NAME, RECIPEID + "=?", new String[]{String.valueOf(ID)});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Returns a specifc recipe object from the database
   *
   * @param recipeID Specify which recipe the query retrieves
   *
   * @return Recipe Object
   */
  @Override
  public Recipe findOneByID(int recipeID) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME + " where " + RECIPEID + " = " + recipeID + ";";
    Cursor cursor = db.rawQuery(query, null);
    RecipeItemsRepo rs = new RecipeItemsRepo();
    Recipe recipe = null;
    cursor.moveToFirst();
    try {
      recipe = new RecipeBuilder().setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)))
          .setName(cursor.getString(cursor.getColumnIndex(NAME)))
          .setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)))
          .setPreparation(cursor.getString(cursor.getColumnIndex(PREPARATION)))
          .setCookingTime(cursor.getInt(cursor.getColumnIndex(COOKINGTIME)))
          .setFavorite(cursor.getInt(cursor.getColumnIndex(FAVORITE)))
          .setHealthRating(cursor.getDouble(cursor.getColumnIndex(HEALTHRATING)))
          .setType(cursor.getString(cursor.getColumnIndex(TYPE)))
          .setImage(cursor.getString(cursor.getColumnIndex(IMAGEID)))
          .setCourse(cursor.getString(cursor.getColumnIndex(COURSE)))
          .setRecipeItems(rs.getRecipeItemBelongingToSpecificRecipe(recipeID))
          .setServings(cursor.getInt(cursor.getColumnIndex(SERVINGS)))
          .createRecipe();
      
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
    }
    return recipe;
    
  }
  
}
