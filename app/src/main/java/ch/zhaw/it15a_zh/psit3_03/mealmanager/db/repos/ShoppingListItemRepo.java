package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.CrudRepository;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handles the specific SQL Statements of the shoppinglistitem Table.
 * Every table has a own TableRepo to handle the specific SQL statments.
 */
public class ShoppingListItemRepo implements CrudRepository<ShoppingListItem> {
  private static final String TABLE_NAME = "shoppinglistitem";
  private static final String COLUMN_SHOPPINGLISTITEMID = "shoppinglistitemID";
  private static final String COLUMN_SHOPPINGLISTID = "shoppinglistID";
  private static final String COLUMN_ITEMID = "itemID";
  private static final String COLUMN_AMOUNT = "amount";
  private static final String COLUMN_BOUGHT = "bought";
  private static final String COLUMN_DATEBOUGHT = "datebought";
  private static final String COLUMN_DATEADDED = "dateadded";
  private static final String COLUMN_LISTGROUPID = "listgroupid";
  private static final String COLUMN_DELETED = "deleted";
  private static final String COLUMN_AMOUNTFROMRECIPES = "amountfromrecipes";
  
  /**
   * Gets a list of all Shoppinglistitems, which have the bought flag set to 1.
   *
   * @return List of Shoppinglistitems.
   */
  public ArrayList<ShoppingListItem> getBoughtShoppinglistitems() {
    String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_BOUGHT + " = 1";
    ArrayList<ShoppingListItem> result = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    while (!cursor.isAfterLast()) {
      String pattern = "yyyy-MM-dd " +
              "HH:mm:ss.SSS";
      String dateboughtAsString = cursor.getString(cursor.getColumnIndex(COLUMN_DATEBOUGHT));
      LocalDateTime datebought = null;
      if (dateboughtAsString != null) {
        datebought = LocalDateTime.parse(dateboughtAsString, DateTimeFormat.forPattern(pattern));
      }
      int shoppinglistitemid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTITEMID));
      int shoppinglistid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTID));
      int itemid = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEMID));
      double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
      int bought = cursor.getInt(cursor.getColumnIndex(COLUMN_BOUGHT));
      LocalDateTime dateadded = LocalDateTime
          .parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATEADDED)), DateTimeFormat.forPattern(pattern));
      int listgroupid = cursor.getInt(cursor.getColumnIndex(COLUMN_LISTGROUPID));
      int deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED));
      double amountFromRecipes = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNTFROMRECIPES));
      ShoppingListItem shoppingListItem =
          new ShoppingListItem(shoppinglistid, itemid, amount, bought, datebought, dateadded, listgroupid,
              amountFromRecipes);
      shoppingListItem.setShoppinglistitemID(shoppinglistitemid);
      shoppingListItem.setDeleted(deleted);
      result.add(shoppingListItem);
      cursor.moveToNext();
    }
    cursor.close();
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Gets a list of all shoppingListItems, which have the deleted flag set to 0.
   *
   * @return List of shoppingListItems.
   */
  public ArrayList<ShoppingListItem> getNotDeleted() {
    String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DELETED + " = 0";
    ArrayList<ShoppingListItem> result = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    while (!cursor.isAfterLast()) {
      String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
      String dateboughtAsString = cursor.getString(cursor.getColumnIndex(COLUMN_DATEBOUGHT));
      LocalDateTime datebought = null;
      if (dateboughtAsString != null) {
        datebought = LocalDateTime.parse(dateboughtAsString, DateTimeFormat.forPattern(pattern));
      }
      int shoppinglistitemid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTITEMID));
      int shoppinglistid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTID));
      int itemid = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEMID));
      double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
      int bought = cursor.getInt(cursor.getColumnIndex(COLUMN_BOUGHT));
      LocalDateTime dateadded = LocalDateTime
          .parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATEADDED)), DateTimeFormat.forPattern(pattern));
      int listgroupid = cursor.getInt(cursor.getColumnIndex(COLUMN_LISTGROUPID));
      int deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED));
      int amountFromRecipes = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNTFROMRECIPES));
      ShoppingListItem shoppingListItem =
          new ShoppingListItem(shoppinglistid, itemid, amount, bought, datebought, dateadded, listgroupid,
              amountFromRecipes);
      shoppingListItem.setShoppinglistitemID(shoppinglistitemid);
      shoppingListItem.setDeleted(deleted);
      result.add(shoppingListItem);
      cursor.moveToNext();
    }
    cursor.close();
    DBManager.getInstance().closeDatabase();
    for (ShoppingListItem temp : result) {
      System.out.println(temp);
    }
    return result;
  }
  
  /**
   * Inserts an shoppingListItem into the database.
   *
   * @param shoppinglistitem The shoppingListItem
   *
   * @return the inserted shoppingListItem
   */
  @Override

  public ShoppingListItem insert(ShoppingListItem shoppinglistitem) {
    ArrayList<ShoppingListItem> tempList = getShoppinglistitemsByItemID(shoppinglistitem.getItemid());
    if (tempList != null && tempList.size() > 0) {
      ShoppingListItem oldShoppinglistitem = tempList.get(0);
      oldShoppinglistitem.setAmount(oldShoppinglistitem.getAmount() + shoppinglistitem.getAmount());
      oldShoppinglistitem
          .setAmountFromRecipes(oldShoppinglistitem.getAmountFromRecipes() + shoppinglistitem.getAmountFromRecipes());
      update(oldShoppinglistitem);
      return oldShoppinglistitem;

    }

    
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    LocalDateTime datebought = shoppinglistitem.getDatebought();
    String dateboughtAsString = null;
    if (datebought != null) {
      dateboughtAsString = datebought.toString();
    }
    ContentValues values = new ContentValues();
    values.put(COLUMN_SHOPPINGLISTID, shoppinglistitem.getShoppinglistid());
    values.put(COLUMN_ITEMID, shoppinglistitem.getItemid());
    values.put(COLUMN_AMOUNT, shoppinglistitem.getAmount());
    values.put(COLUMN_BOUGHT, shoppinglistitem.getBought());
    values.put(COLUMN_DATEBOUGHT, dateboughtAsString);
    values.put(COLUMN_DATEADDED, shoppinglistitem.getDateadded().
        toString());
    values.put(COLUMN_LISTGROUPID, shoppinglistitem.getListgroupid());
    values.put(COLUMN_DELETED, shoppinglistitem.getDeleted());
    values.put(COLUMN_AMOUNTFROMRECIPES, shoppinglistitem.getAmountFromRecipes());
    int id = (int) db.insert(TABLE_NAME, null, values);
    DBManager.getInstance().closeDatabase();
    shoppinglistitem.setShoppinglistitemID(id);
    
    return shoppinglistitem;
  }
  
  /**
   * Gets a list of all Shoppinglistitems.
   *
   * @return List of shoppingListItems.
   */
  @Override
  public ArrayList<ShoppingListItem> findAll() {
    String query = "SELECT * FROM " + TABLE_NAME;
    ArrayList<ShoppingListItem> result = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Cursor cursor = db.rawQuery(query, null);
    
    if (!cursor.moveToFirst()) {
      return null;
    }
    while (!cursor.isAfterLast()) {
      String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
      String dateboughtAsString = cursor.getString(cursor.getColumnIndex(COLUMN_DATEBOUGHT));
      LocalDateTime datebought = null;
      if (dateboughtAsString != null) {
        datebought = LocalDateTime.parse(dateboughtAsString, DateTimeFormat.forPattern(pattern));
      }
      int shoppinglistitemid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTITEMID));
      int shoppinglistid = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTID));
      int itemid = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEMID));
      double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
      int bought = cursor.getInt(cursor.getColumnIndex(COLUMN_BOUGHT));
      LocalDateTime dateadded = LocalDateTime
          .parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATEADDED)), DateTimeFormat.forPattern(pattern));
      int listgroupid = cursor.getInt(cursor.getColumnIndex(COLUMN_LISTGROUPID));
      int deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED));
      int amountFromRecipes = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNTFROMRECIPES));
      ShoppingListItem shoppingListItem =
          new ShoppingListItem(shoppinglistid, itemid, amount, bought, datebought, dateadded, listgroupid,
              amountFromRecipes);
      shoppingListItem.setShoppinglistitemID(shoppinglistitemid);
      shoppingListItem.setDeleted(deleted);
      result.add(shoppingListItem);
      cursor.moveToNext();
    }
    cursor.close();
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Finds an shoppingListItem in the database by its id
   *
   * @return The shoppingListItem
   */
  @Override
  public ShoppingListItem findOneByID(int ID) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME + " where " + COLUMN_SHOPPINGLISTITEMID + " = " + ID + ";";
    Cursor cursor = db.rawQuery(query, null);
    ShoppingListItem shoppingListItem = null;
    if (!cursor.moveToFirst()) {
      return null;
    }
    String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    try {
      String dateboughtAsString = cursor.getString(cursor.getColumnIndex(COLUMN_DATEBOUGHT));
      LocalDateTime datebought = null;
      if (dateboughtAsString != null) {
        datebought = LocalDateTime.parse(dateboughtAsString, DateTimeFormat.forPattern(pattern));
      }
      int shoppinglistitemID = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTITEMID));
      int shoppinglistID = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPINGLISTID));
      int itemID = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEMID));
      double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
      int bought = cursor.getInt(cursor.getColumnIndex(COLUMN_BOUGHT));
      LocalDateTime dateadded = LocalDateTime
          .parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATEADDED)), DateTimeFormat.forPattern(pattern));
      int listgroupID = cursor.getInt(cursor.getColumnIndex(COLUMN_LISTGROUPID));
      int amountFromRecipes = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNTFROMRECIPES));
      int deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED));
      shoppingListItem =
          new ShoppingListItem(shoppinglistID, itemID, amount, bought, datebought, dateadded, listgroupID,
              amountFromRecipes);
      shoppingListItem.setShoppinglistitemID(shoppinglistitemID);
      shoppingListItem.setDeleted(deleted);
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      DBManager.getInstance().closeDatabase();
      cursor.close();
    }
    return shoppingListItem;
  }
  
  /**
   * Updates an shoppingListItem in the database.
   *
   * @param shoppingListItem The updated shoppingListItem
   *
   * @return The number of rows affected
   */
  @Override
  public int update(ShoppingListItem shoppingListItem) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    ContentValues values = new ContentValues();
    LocalDateTime datebought = shoppingListItem.getDatebought();
    String dateboughtAsString = null;
    if (datebought != null) {
      dateboughtAsString = datebought.toString();
    }

    values.put(COLUMN_SHOPPINGLISTID, shoppingListItem.getShoppinglistid());
    values.put(COLUMN_ITEMID, shoppingListItem.getItemid());
    values.put(COLUMN_AMOUNT, shoppingListItem.getAmount());
    values.put(COLUMN_AMOUNTFROMRECIPES, shoppingListItem.getAmountFromRecipes());
    values.put(COLUMN_BOUGHT, shoppingListItem.getBought());
    values.put(COLUMN_DATEADDED, shoppingListItem.getDateadded().toString());

    values.put(COLUMN_DATEBOUGHT, dateboughtAsString);
    values.put(COLUMN_LISTGROUPID, shoppingListItem.getListgroupid());
    values.put(COLUMN_DELETED, shoppingListItem.getDeleted());
    int result = db.update(TABLE_NAME, values, COLUMN_SHOPPINGLISTITEMID + "=?", new String[]{String.valueOf(shoppingListItem.getShoppinglistitemID())});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  public boolean updateAmount(int itemID, double amount) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    
    String query =
        "UPDATE " + TABLE_NAME + " SET " + COLUMN_AMOUNTFROMRECIPES + " = " + COLUMN_AMOUNTFROMRECIPES + " + " +
            amount + " " + "WHERE " + COLUMN_ITEMID + " = " + itemID + ";";
    try {
      db.rawQuery(query, null);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      DBManager.getInstance().closeDatabase();
    }
  }
  
  /**
   * Finds shoppingListItems with specific itemID
   *
   * @param itemID The itemID
   *
   * @return ArrayList of corresponding shoppingListItems.
   */

  public ArrayList<ShoppingListItem> getShoppinglistitemsByItemID(int itemID) {
    if (getNotDeleted() != null && getNotDeleted().size() > 0) {
      ArrayList<ShoppingListItem> itemSpecificList = getNotDeleted();
      Iterator<ShoppingListItem> iterator = itemSpecificList.iterator();
      while (iterator.hasNext()) {
        ShoppingListItem tempItem = iterator.next();

        if (tempItem.getItemid() != itemID) {
          iterator.remove();
        }
      }
      return itemSpecificList;/*
<<<<<<< HEAD
    }else{
      itemSpecificList = new ArrayList<ShoppingListItem>();
      return itemSpecificList;
    }

=======*/
    } else {
      return null;
    }
//>>>>>>> shoppinglistitemsfromweekplan
  }
  
  /**
   * Deletes a shoppingListItem from the Database.
   *
   * @param ID the ID of the shoppingListItem to be removed.
   *
   * @return true if deletion was successful, otherwise false.
   */
  @Override
  public int delete(int ID) {
    ShoppingListItem shoppingListItem = findOneByID(ID);
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    int result = db.delete(TABLE_NAME, COLUMN_SHOPPINGLISTITEMID + "=?", new String[]{String.valueOf(ID)});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  public void deleteRecipeItemFromShoppinglist(int itemID, ShoppingListItem shoppinglistitem) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Log.e("ShoppingListItem ID: " + String.valueOf(findOneByID(shoppinglistitem.getShoppinglistitemID())), "FAIL");
    if (findOneByID(shoppinglistitem.getShoppinglistitemID()).getAmountFromRecipes() -
        shoppinglistitem.getAmountFromRecipes() <= 0) {
      delete(shoppinglistitem.getItemid());
    } else {
      
      String query =
          "UPDATE " + TABLE_NAME + " SET " + COLUMN_AMOUNTFROMRECIPES + " = " + COLUMN_AMOUNTFROMRECIPES + "" + " - " +
              shoppinglistitem.getAmountFromRecipes() + " WHERE " + COLUMN_ITEMID + " = " +
              shoppinglistitem.getItemid() + ";";
      try {
        db.execSQL(query);
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        DBManager.getInstance().closeDatabase();
      }
    }
    
  }
  
  /**
   * Sets the delete flag of an shoppingListItem, with an specific ID.
   *
   * @param ID The id of the shoppingListItem.
   */
  public void setDeleteFlag(int ID) {
    ShoppingListItem shoppingListItem = findOneByID(ID);
    shoppingListItem.setDeleted(1);
    update(shoppingListItem);
  }

}
