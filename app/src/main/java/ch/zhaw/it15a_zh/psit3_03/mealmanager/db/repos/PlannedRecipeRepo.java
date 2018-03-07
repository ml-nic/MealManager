package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;

public class PlannedRecipeRepo {
  private static final String TABLE_NAME = "plannedrecipe";
  private static final String DATEPLANNED = "dateplanned";

  /**
   * Insert list of planned dates into the database.
   * @param plannedDates List of planned dates.
   * @return true, if successful else false
   */
  public boolean insertListPlannedDates(List<String> plannedDates) {
    try {
      SQLiteDatabase db = DBManager.getInstance().openDatabase();
      ContentValues contentValues = new ContentValues();
      for (String date : plannedDates) {
        contentValues.put(DATEPLANNED, date);
        db.insert(TABLE_NAME, null, contentValues);
      }
      return true;
    } catch (SQLiteException e) {
      e.printStackTrace();
      return false;
    } finally {
      DBManager.getInstance().closeDatabase();
    }
  }

  /**
   * Gets all planned dates from the database
   * @return List of all planned dates.
   */
  public List<String> getAllDatesPlanned() {
    List<String> allDatesPlanned = new ArrayList<>();
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    String query = "Select * from " + TABLE_NAME;
    Cursor cursor = db.rawQuery(query, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        allDatesPlanned.add(cursor.getString(cursor.getColumnIndex(DATEPLANNED)));
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {
      e.printStackTrace();
    } finally {
      cursor.close();
      DBManager.getInstance().closeDatabase();
    }
    return allDatesPlanned;
  }

  /**
   * Removes an date from the planned dates.
   * @param date The date to be removed
   * @return true, if successful else false
   */
  public boolean removeDate(String date) {
    SQLiteDatabase db = DBManager.getInstance().openDatabase();
    try {
      db.delete(TABLE_NAME, DATEPLANNED + "=?", new String[]{String.valueOf(date)});
      return true;
    } catch (SQLiteException e) {
      e.printStackTrace();
      return false;
    } finally {
      DBManager.getInstance().closeDatabase();
    }
    
  }
}