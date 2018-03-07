package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.ShoppingListAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ravi on 13.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class ShoppingListItemTest {
    private ShoppingListAdapter shoppingListAdapter;
    private ShoppingListItem zwiebel;
    private ShoppingListItem honig;
    private ShoppingListItem gurke;
    private ArrayList<ShoppingListItem> shoppingListItemArrayList;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        Item zwiebelItem = new Item(1, "Zwiebel", 20, "g");
        Item honigItem = new Item(1, "Zwiebel", 20, "g");
        Item gurkeItem = new Item(1, "Zwiebel", 20, "g");
        shoppingListItemArrayList = new ArrayList<>();
        context = InstrumentationRegistry.getTargetContext();
        shoppingListAdapter = new ShoppingListAdapter(shoppingListItemArrayList, context);

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        zwiebel = new ShoppingListItem(1, 1, 20, 0, null, new LocalDateTime(LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern))), 1, 0);
        honig = new ShoppingListItem(1, 2, 20, 0, null, new LocalDateTime(LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern))), 1, 0);
        gurke = new ShoppingListItem(1, 3, 20, 0, null, new LocalDateTime(LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern))), 1, 0);
        shoppingListItemArrayList.add(zwiebel);
        shoppingListItemArrayList.add(honig);
        shoppingListItemArrayList.add(gurke);
    }

    @Test
    public void addItemTest() {
        shoppingListAdapter.addItemToList("Zwiebel", 20, "g");
        assertEquals(3, shoppingListItemArrayList.size());
    }

    @Test
    public void deleteItemTest(){
        shoppingListAdapter.deleteItemFromList(1);
        assertEquals(2,shoppingListItemArrayList.size());
    }

    @Test
    public void changeBoughtTest(){

    }

    @Test
    public void addAmountTest(){

    }

    @Test
    public void removeAmountTest(){

    }



}
