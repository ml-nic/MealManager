package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.UserPlannedRecipe;

public class UserPlannedRecipesRepo {
  private static final String TABLE_NAME = "userplannedrecipe";
  private static final String RECIPEID = "recipeID";
  private static final String PLANNEDSERVINGS = "plannedservings";
  private static final String DATEPLANNED = "dateplanned";
  
  /**
   * Inserts a new recipe into the weekplanning table
   *
   * @param userPlannedRecipe Recipe which user wants to add to the weekplan
   */
  public void insertUserPlannedRecipe(UserPlannedRecipe userPlannedRecipe) {
    try {
      SQLiteDatabase db = DBManager.getInstance().openDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(RECIPEID, userPlannedRecipe.getRecipeID());
      contentValues.put(PLANNEDSERVINGS, userPlannedRecipe.getPlannedServings());
      contentValues.put(DATEPLANNED, String.valueOf(userPlannedRecipe.getDatePlanned()));
      db.insert(TABLE_NAME, null, contentValues);
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      DBManager.getInstance().closeDatabase();
    }
  }
  
  /**
   * Gets a unique list of all dates for which meals have been planned
   *
   * @return List<String> uniquePlannedDates
   */
  public List<String> getAllUniquePlannedDates() {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    List<String> uniquePlannedDates = new ArrayList<>();
    String query = "Select " + DATEPLANNED + " from " + TABLE_NAME;
    Cursor cursor = db.rawQuery(query, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        uniquePlannedDates.add(cursor.getString(cursor.getColumnIndex(DATEPLANNED)));
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    
    return uniquePlannedDates;
  }
  
  /**
   * Gets a list of all planned Recipes planned on one specific date.
   *
   * @param date Date for which the recipes will be selected by
   *
   * @return List of UserPlannedRecipes.
   */
  public List<UserPlannedRecipe> getUserPlannedRecipeFromSpecificDate(String date) {
    List<UserPlannedRecipe> userPlannedRecipeListFromSpecificDate = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME + " where " + DATEPLANNED + " = " + "'" + date + "'";
    Cursor cursor = db.rawQuery(query, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        UserPlannedRecipe userPlannedRecipe = new UserPlannedRecipe();
        userPlannedRecipe.setDatePlanned(cursor.getString(cursor.getColumnIndex(DATEPLANNED)));
        userPlannedRecipe.setPlannedServings(cursor.getInt(cursor.getColumnIndex(PLANNEDSERVINGS)));
        userPlannedRecipe.setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)));
        userPlannedRecipeListFromSpecificDate.add(userPlannedRecipe);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return userPlannedRecipeListFromSpecificDate;
  }
  
  /**
   * Gets all userPlannedRecipes
   *
   * @return List of UserPlannedRecipes
   */
  public List<UserPlannedRecipe> getAllUserPlannedRecipes() {
    List<UserPlannedRecipe> userPlannedRecipeList = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME;
    Cursor cursor = db.rawQuery(query, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        UserPlannedRecipe userPlannedRecipe = new UserPlannedRecipe();
        userPlannedRecipe.setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)));
        userPlannedRecipe.setPlannedServings(cursor.getInt(cursor.getColumnIndex(PLANNEDSERVINGS)));
        userPlannedRecipe.setDatePlanned(cursor.getString(cursor.getColumnIndex(DATEPLANNED)));
        userPlannedRecipeList.add(userPlannedRecipe);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return userPlannedRecipeList;
  }
  
  /**
   * Removes a planned recipe from the Database
   *
   * @param recipeID ID of Recipe to be removed
   * @param date Date from which the plannedRecipe will be removed
   *
   * @return true if deletion was successful, otherwise false.
   */
  public boolean removePlannedRecipe(int recipeID, String date) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    try {
      db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE '" + date + "' = " + DATEPLANNED + " AND " + recipeID + " = " +
          RECIPEID);
      return true;
    } catch (SQLiteException e) {
      e.printStackTrace();
      return false;
    } finally {
      DBManager.getInstance().closeDatabase();
    }
  }
  
  /**
   * Functionality to check if Recipes have already been planned at a given date (user cannot add identical Recipes
   * to same date).
   *
   * @param date Date to check recipe
   * @param recipeID RecipeID to check
   *
   * @return True if recipe is not planned, False if it is planned.
   */
  public boolean returnTrueIfRecipeIsNotPlannedAtDate(String date, int recipeID) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    
    try {
      db.execSQL(
          "SELECT * FROM " + TABLE_NAME + " WHERE " + RECIPEID + " = " + recipeID + " AND " + DATEPLANNED + " = " +
              "'" + date + "'");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
}
