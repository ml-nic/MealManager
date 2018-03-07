package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ShoppingListViewActivityTest {

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();
    }

    @Rule
    public ActivityTestRule<ShoppingListViewActivity> mShoppingListViewActivityActivityTestRule =
            new ActivityTestRule<>(ShoppingListViewActivity.class);


    @Test
    public void addItem() throws Exception {
        onView(withId(R.id.button_shoppingList_add))
                .perform(click());
        onView(withId(R.id.button_shoppingList_add_finish))
                .check(matches(isDisplayed()));
    }

}