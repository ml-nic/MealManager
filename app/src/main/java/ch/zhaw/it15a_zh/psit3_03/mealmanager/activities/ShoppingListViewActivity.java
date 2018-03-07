package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.CustomSpinnerAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.ShoppingListAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Item;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Unit;

import java.util.ArrayList;

/**
 * {@link ShoppingListViewActivity} is responspible for displaying all the shoppinglistitems.
 * Each item is wrapped with a cardview, which holds all the buttons (plus-, minus-, check-buttons), itemname and amount of the item.
 * And those cardviews are displayed in a RecyclerView.
 * {@link ShoppingListViewActivity} allows the user to create, delete and check an item in the shoppingList.
 */
public class ShoppingListViewActivity extends AppCompatActivity implements ShoppingListAdapter.ItemClickCallback {
    private ShoppingListAdapter adapter;
    private ArrayList<ShoppingListItem> itemList;
    private ItemRepo itemRepo;
    private EditText editTextName;
    private EditText editTextAmount;
    private String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide Top Toolbar form android device
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set LayoutFile
        setContentView(R.layout.activity_shopping_list_view);

     /* BEGIN TOOLBAR */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_shopping_list_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.shopping_list_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    /* END TOOLBAR */

    /*BEGIN Shoppinglist RecyclerView*/
        RecyclerView recView = (RecyclerView) findViewById(R.id.recycler_view);
        recView.setLayoutManager(new LinearLayoutManager(this));
        ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
        itemList = shoppingListItemRepo.getNotDeleted();
        adapter = new ShoppingListAdapter(itemList, this);
        recView.setAdapter(adapter);
    /*END Shoppinglist RecyclerView*/

        //Listeners for clicks and swipes on items
        adapter.setItemClickCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);

        //Buttons set up
        Button addItem = (Button) findViewById(R.id.button_shoppingList_add);

        //Button for adding items. User will be told, if the input is valid or not
        addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ShoppingListViewActivity.this);
                dialog.setTitle("Lebensmittel einfügen");
                dialog.setContentView(R.layout.custom_dialog_shopping);
                dialog.show();

                editTextName = (EditText) dialog.findViewById(R.id.edittext_shopping_list_name);
                editTextAmount = (EditText) dialog.findViewById(R.id.edittext_shopping_list_amount);

                initCustomSpinner(dialog);

                Button addFinish = (Button) dialog.findViewById(R.id.button_shoppingList_add_finish);
                addFinish.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        String itemName = editTextName.getText().toString();
                        String itemAmount = editTextAmount.getText().toString();

                        if (((itemName.trim().isEmpty() || itemName == null) &&
                                (itemAmount.trim().isEmpty() || itemAmount == null))) {
                            Toast.makeText(ShoppingListViewActivity.this, "Leere Eingabe", Toast.LENGTH_SHORT).show();
                        } else if (itemName.trim().isEmpty() || itemName == null) {
                            Toast.makeText(ShoppingListViewActivity.this, "Name ist leer!", Toast.LENGTH_SHORT).show();
                        } else if (itemAmount.trim().isEmpty() || itemAmount == null) {
                            Toast.makeText(ShoppingListViewActivity.this, "Menge ist leer!", Toast.LENGTH_SHORT).show();
                        } else if (itemName.length() > 20) {
                            Toast.makeText(ShoppingListViewActivity.this, "Name ist zu lang!", Toast.LENGTH_SHORT).show();
                        } else if (Double.parseDouble(itemAmount) > 9999) {
                            Toast.makeText(ShoppingListViewActivity.this, "Menge ist zu gross!", Toast.LENGTH_SHORT).show();
                        } else if (Double.parseDouble(itemAmount) <= 0) {
                            Toast.makeText(ShoppingListViewActivity.this, "Ungültige Menge!", Toast.LENGTH_SHORT).show();
                        } else {
                            addItem(itemName, Double.parseDouble(itemAmount), unit);
                            Toast.makeText(ShoppingListViewActivity.this, "Lebensmittel wurde hinzugefügt!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * A list of units (g,kg,l,ml, Stücke) are shown to the user in the dialog,
     * when he wants to add an item. The user can select the appropriate one.
     * Spinner will be populated with data by the {@link CustomSpinnerAdapter}.
     * {@link CustomSpinnerAdapter} will get the data (units) from the enum class {@link Unit}
     *
     * @param dialog a window displaying the input interface
     */
    private void initCustomSpinner(Dialog dialog) {
        Spinner spinnerCustom = (Spinner) dialog.findViewById(R.id.spinnerCustom);
        ArrayList<String> units = new ArrayList<>();
        for (Unit unit : Unit.values()) {
            units.add(unit.name());
        }

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(ShoppingListViewActivity.this, units);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //One element will always be selected, no need for implementation
            }
        });
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }

                };
        return simpleItemTouchCallback;
    }


    /**
     * Certain amount will be added to the selected item.
     * First of all {@link ShoppingListViewActivity} checks
     * how much the increase will be. The increase is given as a parameter
     * to {@ShoppingListAdapter}, where the calculation happens.=> (ModelViewController-Pattern)
     * {@ShoppingListAdapter} is the controller in this case.
     *
     * @param adapterPosition tells the position in the arrayList of the selected item.
     */
    @Override
    public void addAmount(int adapterPosition) {
        itemRepo = new ItemRepo();
        itemList = adapter.getItemList();
        ShoppingListItem shoppingListItem = itemList.get(adapterPosition);
        Item item = itemRepo.findOneByID(shoppingListItem.getItemid());
        String unit = item.getUnit().toLowerCase();
        double amount = shoppingListItem.getAmount();
        double increase = 0.0;
        if (unit.matches("g") && !(amount + 100 >= 9999)) {
            increase = 100;
        } else if (unit.matches("kg") && !(amount + 0.1 >= 9999)) {
            increase = 0.1;
        } else if (unit.matches("ml") && !(amount + 100 >= 9999)) {
            increase = 100;
        } else if (unit.matches("l") && !(amount + 0.1 >= 9999)) {
            increase = 0.1;
        } else if (unit.matches("stück") && !(amount + 1 >= 9999)) {
            increase = 1;
        } else if (unit.matches("tl") && !(amount + 1 >= 9999)) {
            increase = 1;
        }

        adapter.addAmount(shoppingListItem, increase);
    }

    /**
     * Certain amount will be subtracted from the selected item.
     * First of all {@link ShoppingListViewActivity} checks
     * how much the decrease will be. The decrease is given as a parameter
     * to {@ShoppingListAdapter}, where the calculation happens.=> (ModelViewController-Pattern)
     * {@ShoppingListAdapter} is the controller in this case. Also a Toast message will appear
     * if the the user is removing too much amount, whether because it is mathematically not possible
     * or because the rest of the amount belongs to a recipe.
     *
     * @param adapterPosition tells the position in the arrayList of the selected item.
     */
    @Override
    public void removeAmount(int adapterPosition) {
        itemRepo = new ItemRepo();
        itemList = adapter.getItemList();
        ShoppingListItem shoppingListItem = itemList.get(adapterPosition);
        Item item = itemRepo.findOneByID(shoppingListItem.getItemid());
        String unit = item.getUnit().toLowerCase();
        double amount = shoppingListItem.getAmount();
        double decrease = 0.0;
        if (unit.matches("g")) {
            decrease = -100;
        } else if (unit.matches("kg")) {
            decrease = -0.1;
        } else if (unit.matches("ml")) {
            decrease = -100;
        } else if (unit.matches("l")) {
            decrease = -0.1;
        } else if (unit.matches("stück")) {
            decrease = -1;
        } else if (unit.matches("tl")) {
            decrease = -1;
        }
        if (amount < -decrease && shoppingListItem.getAmountFromRecipes() > 0) {
            String message = "Es wurden nur " + amount + " " + unit + " " + " entfernt, da der Rest zu einem Rezept gehört";
            decrease = -amount;
            if (amount == 0) {
                message = "Die Menge kann nicht mehr verkleinert werden, der Rest gehört zu Rezepten.";
                Toast.makeText(ShoppingListViewActivity.this, message, Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(ShoppingListViewActivity.this, message, Toast.LENGTH_LONG).show();
        } else if (amount <= -decrease && shoppingListItem.getAmountFromRecipes() == 0) {
            Toast.makeText(ShoppingListViewActivity.this, "Die Menge kann nicht mehr verkleinert werden.", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        adapter.removeAmount(shoppingListItem, decrease);
    }

    /**
     * The selected item is going to be checked or unchecked,
     * in by clicking the check-Button.
     * adapterPosition tells the position in the arrayList
     * of the selected item.
     *
     * @param adapterPosition tells the position in the arrayList of the selected item.
     */
    @Override
    public void changeBought(int adapterPosition) {
        adapter.changeBought(adapter.getItemList().get(adapterPosition));
        adapter.notifyItemChanged(adapterPosition);
    }

    /**
     * By clicking on the name of the item the full name
     * will be shown in a dialog. In the dialog by clicking
     * on the name again, the dialog will be dismissed.
     * adapterPosition tells the position in the arrayList
     * of the selected item.
     *
     * @param adapterPosition tells the position in the arrayList of the selected item.
     */
    @Override
    public void showFullName(int adapterPosition) {

        final Dialog dialog = new Dialog(ShoppingListViewActivity.this);
        dialog.setContentView(R.layout.custom_dialog_shopping_item_name);
        dialog.show();

        ItemRepo itemRepo = new ItemRepo();
        Item item = itemRepo.findOneByID(adapter.getItemList().get(adapterPosition).getItemid());
        TextView textViewItemName = (TextView) dialog.findViewById(R.id.textView_shopping_list_itemname);
        textViewItemName.setText(item.getName());

        textViewItemName.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    /**
     * By clicking the additem button the user can
     * create a new item, which will be added to the {@link ShoppingListAdapter}
     * Therefore the user has to enter name, amount and unit in  order
     * to create an item. New Item will  be added to bottom of the list.
     *
     * @param name   The name of the item
     * @param amount The amount of the item
     * @param unit   The unit of the item
     */
    private void addItem(String name, double amount, String unit) {
        adapter.addItemToList(name, amount, unit);
    }


    /**
     * Currently not working and also there is not much of
     * a need or use now. This method was added to this class, because
     * in {@link ItemTouchHelper} the method {@link ItemTouchHelper.SimpleCallback onMove}
     * has to be implemented. Old position and new Position are necessary parameters
     * in order to move the item to the desired position in the list.
     *
     * @param oldPos The oldPosition of the item
     * @param newPos The newPosition of the item
     */
    private void moveItem(int oldPos, int newPos) {
        adapter.notifyItemMoved(oldPos, newPos);
    }


    /**
     * Deletes an item from the list. (Deletion will happen in {@link ShoppingListAdapter})
     * {@link ShoppingListAdapter} will accordingly refresh the list after the deletion
     *
     * @param adapterPosition tells the position in the arrayList of the selected item.
     */
    private void deleteItem(final int adapterPosition) {
        adapter.deleteItemFromList(adapterPosition);
    }


    public ArrayList getItemList() {
        return itemList;
    }


}
