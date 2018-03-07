package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeItem;

/**
 * Handles the specific SQL Statements of the recipeItems Table.
 * Every table has a own TableRepo to handle the specific SQL statments.
 */
public class RecipeItemsRepo {
  private static final String TABLE_NAME = "recipeitems";
  private static final String RECIPEITEMID = "recipeItemsID";
  private static final String ITEMID = "itemID";
  private static final String RECIPEID = "recipeID";
  private static final String AMOUNT = "amount";
  private static final String UNIT = "unit";
  
  /**
   * Returns an ArrayList<RecipeItem> recipeItemList of all recipeItems in the Database
   *
   * @return ArrayList<RecipeItem> recipeItemList
   */
  public ArrayList<RecipeItem> findAll() {
    ArrayList<RecipeItem> recipeItems = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME;
    Cursor cursor = db.rawQuery(query, null);
    
    if (!cursor.moveToFirst()) {
      return null;
    }
    try {
      while (!cursor.isAfterLast()) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setRecipeItemID(cursor.getInt(cursor.getColumnIndex(RECIPEITEMID)));
        recipeItem.setItemID(cursor.getInt(cursor.getColumnIndex(ITEMID)));
        recipeItem.setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)));
        recipeItem.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
        recipeItem.setAmount(cursor.getDouble(cursor.getColumnIndex(AMOUNT)));
        recipeItems.add(recipeItem);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
      
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return recipeItems;
  }
  
  /**
   * Retrieves all the recipeItems in regards to a specific recipe. This includes the itemID, the item Unit and the
   * amount used in the specifc recipe
   *
   * @param recipeID Specifies the recipe whose recipeItems are being asked to returned
   *
   * @return ArrayList<RecipeItem> items belonging to a specific recipe
   */
  public ArrayList<RecipeItem> getRecipeItemBelongingToSpecificRecipe(int recipeID) {
    ArrayList<RecipeItem> recipeItems = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME + " where " + RECIPEID + " = " + recipeID;
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    try {
      while (!cursor.isAfterLast()) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setRecipeItemID(cursor.getInt(cursor.getColumnIndex(RECIPEITEMID)));
        recipeItem.setRecipeID(cursor.getInt(cursor.getColumnIndex(RECIPEID)));
        recipeItem.setItemID(cursor.getInt(cursor.getColumnIndex(ITEMID)));
        recipeItem.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
        recipeItem.setAmount(cursor.getDouble(cursor.getColumnIndex(AMOUNT)));
        recipeItems.add(recipeItem);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
    }
    return recipeItems;
  }
}
