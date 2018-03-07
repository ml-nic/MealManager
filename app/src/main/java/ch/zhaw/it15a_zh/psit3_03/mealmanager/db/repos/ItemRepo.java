package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Item;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.CrudRepository;

/**
 * Handles the specific SQL Statements of the Item Table.
 * Every table has a own TableRepo to handle the specific SQL statments.
 */
public class ItemRepo implements CrudRepository<Item> {
  public static final String TABLE_NAME = "item";
  private static final String ITEMID = "itemID";
  private static final String NAME = "name";
  private static final String UNIT = "unit";
  
  /**
   * Inserts an itemObject into the item Table. An Item inclides a Name (String), Unit (String) and ItemID(int,
   * primary key, auto increment)
   *
   * @param item takes an item object which will be added to the bottom of the item table
   */
  @Override
  public Item insert(Item item) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    ContentValues values = new ContentValues();
    values.put(NAME, item.getName());
    values.put(UNIT, item.getUnit());
    long id = db.insert(TABLE_NAME, null, values);
    DBManager.getInstance().closeDatabase();
    item.setItemID((int) id);
    return item;
  }
  
  /**
   * Returns a list of all items currently in the item table.
   *
   * @return ArrayList<Item> all items in the item table.
   */
  @Override
  public ArrayList<Item> findAll() {
    String query = "SELECT * FROM " + TABLE_NAME;
    ArrayList<Item> items = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    try {
      while (!cursor.isAfterLast()) {
        Item item = new Item();
        item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        item.setItemID(cursor.getInt(cursor.getColumnIndex(ITEMID)));
        item.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
        items.add(item);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return items;
  }
  
  /**
   * Updates an Item in the Item Table. Takes a int itemID to specify which item is to be replaced and an Item Object
   * to replace the item in the table.
   *
   * @param item Item Object which will replace the old item in the table
   */
  @Override
  public int update(Item item) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    ContentValues values = new ContentValues();
    values.put(NAME, item.getName());
    values.put(UNIT, item.getUnit());
    int result = db.update(TABLE_NAME, values, ITEMID + "=?", new String[]{String.valueOf(item.getItemID())});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Delets a Item from the item table
   *
   * @param id The id of the item, which should be removed.
   */
  @Override
  public int delete(int id) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    int result = db.delete(TABLE_NAME, "itemID=?", new String[]{String.valueOf(id)});
    DBManager.getInstance().closeDatabase();
    return result;
  }
  
  /**
   * Returns an item, with the specific itemID
   *
   * @param itemID id in Table of item to get
   *
   * @return returns a Item object
   */
  @Override
  public Item findOneByID(int itemID) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME + " where " + ITEMID + " = " + itemID + ";";
    Item item = new Item();
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    try {
      item.setItemID(cursor.getInt(cursor.getColumnIndex(ITEMID)));
      item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
      item.setUnit(cursor.getString(cursor.getColumnIndex(UNIT)));
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return item;
  }
  
  /**
   * Find an item in the database by name.
   *
   * @param itemName The name of the item.
   *
   * @return The item with the specific name.
   */
  public Item findOneByName(String itemName) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " = '" + itemName + "';";
    Item item = null;
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      return null;
    }
    try {
      int itemID = cursor.getInt(cursor.getColumnIndex(ITEMID));
      String unit = cursor.getString(cursor.getColumnIndex(UNIT));
      item = new Item(itemID, itemName, 0, unit);
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return item;
  }
  
  /**
   * Checks if the item already exists in the database, if not it will be inserted into the database.
   *
   * @param item The item which should be inserted to the database.
   *
   * @return The real item, use this instead of your old item, just override the old one.
   */
  public Item safeInsert(Item item) {
    Item existingItem = findOneByName(item.getName());
    if (existingItem == null) {
      insert(item);
      existingItem = findOneByName(item.getName());
    }
    return existingItem;
  }
}