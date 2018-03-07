package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.RecipeOverviewAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.SortState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class RecipeTest {

    private String mStringToBetyped;
    private RecipeOverviewAdapter roa;
    private Recipe pizza;
    private Recipe ananas;
    private Recipe hotdog;

    @Before
    public void setUp() {
        pizza = new Recipe(0, "Pizza", "Yummy dish", "Cook", "pizza.jpg", 10,
                0, 1, 1, "Main meal", new ArrayList<RecipeItem>(), "Frühstück");
        ananas = new Recipe(0, "Ananas", "Yummy dish", "Cook", "ananas.jpg", 10,
                2, 1, 1, "Main meal", new ArrayList<RecipeItem>(), "Mittagessen");
        hotdog = new Recipe(0, "Hotdog", "Yummy dish", "Cook", "hotdog.jpg", 10,
                1, 1, 1, "Main meal", new ArrayList<RecipeItem>(), "Snack");
        List<Recipe> currentRecipesList = new ArrayList<>();
        currentRecipesList.add(pizza);
        currentRecipesList.add(ananas);
        currentRecipesList.add(hotdog);

        Context context = InstrumentationRegistry.getTargetContext();
        roa = new RecipeOverviewAdapter(context, currentRecipesList, "dateHeader");
    }

    @Test
    public void sortNameAscendingTest() {
        List<Recipe> sortedList = new ArrayList<>();
        sortedList.add(ananas);
        sortedList.add(hotdog);
        sortedList.add(pizza);
        roa.sortRecipesByName(SortState.ASCENDING);

        assertEquals(sortedList, roa.getCurrentRecipeList());
    }

    @Test
    public void sortNameDescendingTest() {
        List<Recipe> sortedList = new ArrayList<>();
        sortedList.add(pizza);
        sortedList.add(hotdog);
        sortedList.add(ananas);
        roa.sortRecipesByName(SortState.DESCENDING);

        assertEquals(sortedList, roa.getCurrentRecipeList());
    }

    @Test
    public void sortNameNoneTest() {
        List<Recipe> sortedList = new ArrayList<>();
        sortedList.add(pizza);
        sortedList.add(ananas);
        sortedList.add(hotdog);
        roa.sortRecipesByName(SortState.NONE);

        assertEquals(sortedList, roa.getCurrentRecipeList());
    }
}