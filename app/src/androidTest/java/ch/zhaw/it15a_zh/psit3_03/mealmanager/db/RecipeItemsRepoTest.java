package ch.zhaw.it15a_zh.psit3_03.mealmanager.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeItemsRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeItemsRepoTest {
    private RecipeItemsRepo recipeItemsRepo = null;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        recipeItemsRepo = new RecipeItemsRepo();
    }
            
    @Test
    public void findAll_isCorrect() throws Exception {
        ArrayList<RecipeItem> recipeList = recipeItemsRepo.findAll();
        assertEquals(57, recipeList.size());
    }

    @Test
    public void getRecipeItemBelongingToSpecificRecipe_isCorrect() throws  Exception {
        ArrayList<RecipeItem> recipeList = recipeItemsRepo.getRecipeItemBelongingToSpecificRecipe(1);
        assertEquals(18, recipeList.size());
    }

}
