package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Item;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ShoppingListAdapterTest {

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
    }

    @Test
    public void addItemTest() {
        shoppingListItemArrayList.add(zwiebel);
        shoppingListItemArrayList.add(honig);
        shoppingListItemArrayList.add(gurke);
        shoppingListAdapter.addItemToList("Zwiebel", 20, "g");

        assertEquals(6, shoppingListItemArrayList.size() + 3);
    }
}
