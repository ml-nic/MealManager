package ch.zhaw.it15a_zh.psit3_03.mealmanager.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeRepoTest {
    private RecipeRepo recipeRepo = null;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        recipeRepo = new RecipeRepo();
    }

    @Test
    public void findAll_isCorrect() throws Exception {
        ArrayList<Recipe> recipeList = recipeRepo.findAll();
        assertEquals(4, recipeList.size());
    }

    @Test
    public void findOneByID_isCorrect() throws Exception {
        Recipe expectedItem = new RecipeBuilder()
                .setRecipeID(1)
                .setName("Gemüse - Reis - Pfanne").createRecipe();
        Recipe actualItem = recipeRepo.findOneByID(1);

        boolean actualValue = actualItem.equals(expectedItem);
        assertEquals(true, actualValue);
    }


    @Test
    public void insert_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        Recipe expectedItem = new RecipeBuilder()
                .setName("TestRecipeInsert").createRecipe();

        expectedItem = recipeRepo.insert(expectedItem);
        expectedItem = recipeRepo.findOneByID(expectedItem.getRecipeID());

        assertNotNull(expectedItem);

        recipeRepo.delete(expectedItem.getRecipeID());
    }

    @Test
    public void update_isCorrect() throws Exception {
        Recipe expectedItem = new RecipeBuilder()
                .setName("TestRecipeUpdate").createRecipe();
        expectedItem = recipeRepo.insert(expectedItem);

        expectedItem.setType("newType");
        recipeRepo.update(expectedItem);

        expectedItem = recipeRepo.findOneByID(expectedItem.getRecipeID());

        assertEquals(expectedItem.getType(), expectedItem.getType());

        recipeRepo.delete(expectedItem.getRecipeID());
    }


    @Test
    public void delete_isCorrect() throws Exception {
        Recipe expectedItem = new RecipeBuilder().setName("TesRecipeDelete").createRecipe();
        expectedItem = recipeRepo.insert(expectedItem);

        recipeRepo.delete(expectedItem.getRecipeID());

        ArrayList<Recipe> recipeList = recipeRepo.findAll();

        boolean actualValue = true;
        for (Recipe item : recipeList) {
            if (item.getRecipeID() == expectedItem.getRecipeID()) {
                actualValue = false;
            }
        }
        assertEquals(4, recipeList.size());
        assertEquals(true, actualValue);

    }

}
