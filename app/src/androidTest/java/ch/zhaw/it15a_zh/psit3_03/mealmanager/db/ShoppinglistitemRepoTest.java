package ch.zhaw.it15a_zh.psit3_03.mealmanager.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
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
public class ShoppingListItemRepoTest {
    private ShoppingListItemRepo shoppinglistitemRepo = null;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();

        DBHelper dbHelper = new DBHelper(context, "mmdb_test");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        shoppinglistitemRepo = new ShoppingListItemRepo();
    }

    @Test
    public void findAll_isCorrect() throws Exception {
        ArrayList<ShoppingListItem> shoppingListItemList = shoppinglistitemRepo.findAll();
        assertEquals(174, shoppingListItemList.size());
    }

    @Test
    public void findOneByID_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2015-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        LocalDateTime datebought = LocalDateTime.parse("2015-10-07T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistitemID(1);
        expectedItem.setShoppinglistid(1);
        expectedItem.setItemid(1);
        expectedItem.setAmount(7);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(datebought);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        ShoppingListItem actualItem = shoppinglistitemRepo.findOneByID(1);

        boolean actualValue = actualItem.equals(expectedItem);
        assertEquals(true, actualValue);
    }

    @Test
    public void findOneByIDDateboughtIsNullTest() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2015-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(null);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        ShoppingListItem actualItem = shoppinglistitemRepo.findOneByID(expectedItem.getShoppinglistitemID());

        boolean actualValue = actualItem.equals(expectedItem);
        assertEquals(true, actualValue);
        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void setDeleteFlagDateboughtIsNullTest() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(null);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        shoppinglistitemRepo.setDeleteFlag(expectedItem.getShoppinglistitemID());

        ArrayList<ShoppingListItem> shoppingListItemList = shoppinglistitemRepo.findAll();

        boolean actualValue = false;
        for (ShoppingListItem item : shoppingListItemList) {
            if (item.getShoppinglistitemID() == expectedItem.getShoppinglistitemID()) {
                if (item.getDeleted() == 1) {
                    actualValue = true;
                }
            }
        }
        assertEquals(true, actualValue);
        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void setDeleteFlag_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        LocalDateTime datebought = LocalDateTime.parse("2017-10-07T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(datebought);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        shoppinglistitemRepo.setDeleteFlag(expectedItem.getShoppinglistitemID());

        ArrayList<ShoppingListItem> shoppingListItemList = shoppinglistitemRepo.findAll();

        boolean actualValue = false;
        for (ShoppingListItem item : shoppingListItemList) {
            if (item.getShoppinglistitemID() == expectedItem.getShoppinglistitemID()) {
                if (item.getDeleted() == 1) {
                    actualValue = true;
                }
            }
        }
        assertEquals(true, actualValue);
        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void update_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        LocalDateTime datebought = LocalDateTime.parse("2017-10-07T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(datebought);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        expectedItem.setAmount(10);
        shoppinglistitemRepo.update(expectedItem);

        expectedItem = shoppinglistitemRepo.findOneByID(expectedItem.getShoppinglistitemID());

        assertEquals(expectedItem.getAmount(), expectedItem.getAmount(), 0.01);

        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void updateDateboughtNullTest() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));


        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(null);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        expectedItem.setAmount(10);
        shoppinglistitemRepo.update(expectedItem);

        expectedItem = shoppinglistitemRepo.findOneByID(expectedItem.getShoppinglistitemID());

        assertEquals(expectedItem.getAmount(), expectedItem.getAmount(), 0.01);

        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void insert_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        LocalDateTime datebought = LocalDateTime.parse("2017-10-07T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(datebought);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        expectedItem = shoppinglistitemRepo.findOneByID(expectedItem.getShoppinglistitemID());

        assertNotNull(expectedItem);

        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void insertDateboughtNullTest() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(null);
        expectedItem.setBought(1);
        expectedItem.setDeleted(0);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);

        expectedItem = shoppinglistitemRepo.findOneByID(expectedItem.getShoppinglistitemID());

        assertNotNull(expectedItem);

        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());
    }

    @Test
    public void getBoughtShoppinglistitems_isCorrect() throws Exception {
        ArrayList<ShoppingListItem> shoppinglistitemsList = shoppinglistitemRepo.getBoughtShoppinglistitems();
        int actualSize = shoppinglistitemsList.size();

        assertEquals(172, actualSize);
    }

    @Test
    public void getNotDeleted_isCorrect() throws Exception {
        ArrayList<ShoppingListItem> shoppinglistitemsList = shoppinglistitemRepo.getNotDeleted();
        int actualSize = shoppinglistitemsList.size();

        assertEquals(5, actualSize);
    }

    @Test
    public void delete_isCorrect() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        LocalDateTime datebought = LocalDateTime.parse("2017-10-07T00:00:00.000", DateTimeFormat.forPattern(pattern));

        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(datebought);
        expectedItem.setBought(1);
        expectedItem.setDeleted(1);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);


        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());

        ArrayList<ShoppingListItem> shoppingListItemList = shoppinglistitemRepo.findAll();

        boolean actualValue = true;
        for (ShoppingListItem item : shoppingListItemList) {
            if (item.getShoppinglistitemID() == expectedItem.getShoppinglistitemID()) {
                actualValue = false;
            }
        }
        assertEquals(174, shoppingListItemList.size());
        assertEquals(true, actualValue);

    }

    @Test
    public void deleteDateBoughIsNullTest() throws Exception {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        LocalDateTime dateadded = LocalDateTime.parse("2017-10-05T00:00:00.000", DateTimeFormat.forPattern(pattern));
        ShoppingListItem expectedItem = new ShoppingListItem();
        expectedItem.setShoppinglistid(5);
        expectedItem.setItemid(5);
        expectedItem.setAmount(5);
        expectedItem.setDateadded(dateadded);
        expectedItem.setDatebought(null);
        expectedItem.setBought(1);
        expectedItem.setDeleted(1);
        expectedItem = shoppinglistitemRepo.insert(expectedItem);


        shoppinglistitemRepo.delete(expectedItem.getShoppinglistitemID());

        ArrayList<ShoppingListItem> shoppingListItemList = shoppinglistitemRepo.findAll();

        boolean actualValue = true;
        for (ShoppingListItem item : shoppingListItemList) {
            if (item.getShoppinglistitemID() == expectedItem.getShoppinglistitemID()) {
                actualValue = false;
            }
        }
        assertEquals(174, shoppingListItemList.size());
        assertEquals(true, actualValue);
    }

}
