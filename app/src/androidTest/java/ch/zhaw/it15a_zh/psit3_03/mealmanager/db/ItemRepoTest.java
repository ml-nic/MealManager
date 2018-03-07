package ch.zhaw.it15a_zh.psit3_03.mealmanager.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Item;
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
public class ItemRepoTest {
    private ItemRepo itemRepo = null;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        itemRepo = new ItemRepo();
    }

    @Test
    public void findAll_isCorrect() throws Exception {
        ArrayList<Item> itemList = itemRepo.findAll();
        assertEquals(45, itemList.size());
    }

    @Test
    public void findOneByID_isCorrect() throws Exception {
        Item expectedItem = new Item();
        expectedItem.setItemID(1);
        expectedItem.setName("Zwiebel");
        expectedItem.setUnit("stück");

        Item actualItem = itemRepo.findOneByID(1);

        boolean actualValue = actualItem.equals(expectedItem);
        assertEquals(true, actualValue);
    }

    @Test
    public void findOneByName_isCorrect() throws Exception {
        Item expectedItem = new Item();
        expectedItem.setItemID(1);
        expectedItem.setName("Zwiebel");
        expectedItem.setUnit("stück");

        Item actualItem = itemRepo.findOneByName("Zwiebel");

        boolean actualValue = actualItem.equals(expectedItem);
        assertEquals(true, actualValue);
    }

    @Test
    public void delete_isCorrect() throws Exception {
        Item expectedItem = new Item();
        expectedItem.setName("TestDelete");
        expectedItem.setUnit("stück");
        expectedItem = itemRepo.safeInsert(expectedItem);

        itemRepo.delete(expectedItem.getItemID());

        ArrayList<Item> itemList = itemRepo.findAll();

        boolean actualValue = true;
        for(Item item : itemList) {
            if(item.getItemID() == expectedItem.getItemID()) {
                actualValue = false;
            }
        }
        assertEquals(45, itemList.size());
        assertEquals(true, actualValue);

    }

    @Test
    public void update_isCorrect() throws Exception {
        String expectedName = "UpdateWorked";

        Item expectedItem = new Item();
        expectedItem.setName("TestUpdate");
        expectedItem.setUnit("stück");
        expectedItem = itemRepo.safeInsert(expectedItem);

        expectedItem.setName(expectedName);
        itemRepo.update(expectedItem);

        expectedItem = itemRepo.findOneByID(expectedItem.getItemID());

        assertEquals(expectedName, expectedItem.getName());

        itemRepo.delete(expectedItem.getItemID());
    }

    @Test
    public void insert_isCorrect() throws Exception {
        Item expectedItem = new Item();
        expectedItem.setName("TestInsert");
        expectedItem.setUnit("stück");
        expectedItem = itemRepo.insert(expectedItem);

        assertNotNull(expectedItem);

        itemRepo.delete(expectedItem.getItemID());
    }

    @Test
    public void safeInsert_isCorrect() throws Exception {
        Item expectedItem = new Item();
        expectedItem.setName("TestSafeInsert");
        expectedItem.setUnit("stück");
        expectedItem = itemRepo.safeInsert(expectedItem);

        int actualSize = itemRepo.findAll().size();

        assertEquals(46, actualSize);

        itemRepo.delete(expectedItem.getItemID());

    }

}
