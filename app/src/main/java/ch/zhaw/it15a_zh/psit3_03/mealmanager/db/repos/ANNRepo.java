package ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.ann.ANN;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Handles the specific SQL Statements of the ANN Table.
 * Every table has a own TableRepo to handle the specific SQL statments.
 */
public class ANNRepo {
    private static final String TABLE_NAME = "ann";
    private static final String COLUMN_ID = "annid";
    private static final String COLUMN_OBJECT_JSON = "objectjson";

    /**
     * Insert an ann into table "ann"
     *
     * @param ann The ANN which should be inserted into the database
     */
    public ANN insert(ANN ann) {
        String serializedObject = serializeObj(ann);

        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OBJECT_JSON, serializedObject);
        db.insert(TABLE_NAME, null, values);
        DBManager.getInstance().closeDatabase();
        return ann;
    }

    /**
     * Gets all ann´s from the database.
     *
     * @return all ann´s in an arrayList
     */
    public ArrayList<ANN> findAll() {
        String query = "SELECT * FROM " + TABLE_NAME;
        ArrayList<ANN> result = new ArrayList<>();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String objectAsString = cursor.getString(cursor.getColumnIndex(COLUMN_OBJECT_JSON));
            ANN obj = deserializeObject(objectAsString);
            result.add(obj);
            cursor.moveToNext();
        }
        cursor.close();
        DBManager.getInstance().closeDatabase();
        return result;
    }

    /**
     * Updates a existing item.
     *
     * @param ann The new ann
     */
    public int update(ANN ann) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        String serializedObject = serializeObj(ann);

        values.put(COLUMN_OBJECT_JSON, serializedObject);
        int result = db.update(TABLE_NAME, values, "annid=?", new String[]{String.valueOf(ann.getANNID())});
        DBManager.getInstance().closeDatabase();
        return result;
    }

    /**
     * Deletes one ann in the database
     *
     * @param ID The ID of the ann which should be removed.
     */
    public int delete(int ID) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        int result = db.delete(TABLE_NAME, "annid=?", new String[]{String.valueOf(ID)});
        DBManager.getInstance().closeDatabase();
        return result;
    }

    /**
     * Get latest ann from the database.
     *
     * @return The latest ann.
     */
    public ANN getCurrentANN() {
        String query = "SELECT * FROM " + TABLE_NAME;
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            return null;
        }
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(1));
            cursor.moveToNext();
        }

        ANN obj = null;
        if (result.size() > 0) {
            obj = deserializeObject(result.get(result.size() - 1));
        }
        cursor.close();
        DBManager.getInstance().closeDatabase();
        return obj;
    }

    private ANN deserializeObject(String objString) {
        ANN obj = null;
        try {
            byte b[] = objString.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (ANN) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    private String serializeObj(ANN ann) {
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(ann);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return serializedObject;
    }

}