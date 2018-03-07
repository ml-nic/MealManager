package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.MainActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void openShoppingListViewActivity_Test() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ShoppingListViewActivity.class.getName(), null, false);

        // open current activity.
        MainActivity mainActivity = mainActivityTestRule.getActivity();
        final Button button = (Button) mainActivity.findViewById(R.id.button_shoppingList);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        ShoppingListViewActivity shoppingListViewActivity = (ShoppingListViewActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(shoppingListViewActivity);
        shoppingListViewActivity.finish();
    }

    @Test
    public void openRecipeOverviewActivity_Test() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RecipeOverviewActivity.class.getName(), null, false);

        MainActivity mainActivity = mainActivityTestRule.getActivity();
        final Button button = (Button) mainActivity.findViewById(R.id.button_recipes);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        RecipeOverviewActivity recipeOverviewActivity = (RecipeOverviewActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(recipeOverviewActivity);
        recipeOverviewActivity.finish();
    }

    @Test
    public void openWeekplanActivity() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(WeekPlanActivity.class.getName(), null, false);

        MainActivity mainActivity = mainActivityTestRule.getActivity();
        final Button button = (Button) mainActivity.findViewById(R.id.button_meal_proposal);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        WeekPlanActivity weekPlanActivity = (WeekPlanActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(weekPlanActivity);
        weekPlanActivity.finish();
    }

}