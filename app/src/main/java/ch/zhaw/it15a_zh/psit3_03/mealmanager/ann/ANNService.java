package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;

/**
 * Service to run ANNHandler in new Thread and to hold this Thread alive if the main thread was destroyed.
 */
public class ANNService extends Service {
    /**
     * Entry point of the service start.
     *
     * @param intent  The intent from the caller.
     * @param flags   Flag for different behaviour.
     * @param startId Id of this service
     * @return code for different handling if service destroyed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Start a new Thread for parallel computation of the neural network
        Thread annThread = new Thread("AnnService(" + startId + ")") {
            @Override
            public void run() {
                DBHelper dbHelper = new DBHelper(getApplicationContext(), "mmdb_presentation");
                DBManager.initializeInstance(dbHelper);
                dbHelper.createDataBase();

                ANNHandler annHandler = new ANNHandler();
                annHandler.start();
                stopSelf();
            }
        };
        annThread.start();
        return Service.START_STICKY;
    }

    /**
     * Returns always null, because no communication between service and caller.
     *
     * @param intent The intent
     * @return always null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}