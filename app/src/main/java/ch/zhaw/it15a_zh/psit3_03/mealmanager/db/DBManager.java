package ch.zhaw.it15a_zh.psit3_03.mealmanager.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Handles the instance of the database.
 */
public class DBManager {
    private static DBManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private Integer mOpenCounter = 0;
    private SQLiteDatabase mDatabase;

    /**
     * Initialize the Instance of DBHelper and DBManager,
     * so that only one instance of each exists.
     */
    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DBManager();
            mDatabaseHelper = helper;
        }
    }

    /**
     * Returns the instance of DBManager,
     * if none exist one will be created.
     *
     * @return the instance of the DBManager
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    DBManager.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    /**
     * Opens the database connection and counts how many connections are open.
     *
     * @return the writable Instance of the database
     */
    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter += 1;
        if (mOpenCounter == 1) {
            // Opening new ch.zhaw.it15a_zh.psit3_03.mealmanager.database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Close the database connection and reduce the mOpenCounter.
     */
    public synchronized void closeDatabase() {
        mOpenCounter -= 1;
        if (mOpenCounter == 0) {
            // Closing ch.zhaw.it15a_zh.psit3_03.mealmanager.database
            mDatabase.close();

        }
    }
}
