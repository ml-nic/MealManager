package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.ShoppingListViewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Item;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.joda.time.LocalDateTime;


/**
 * The ShoppingListAdapter is responsible for putting the shoppinglistitems into an adapter
 * which will converts the several shoppinglistitems (arrayList) into a viewable list in
 * {@link ShoppingListViewActivity} with the according buttons, name and amount
 * (=> shortly said responsible for preparing the content).
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder> {
  private ArrayList<ShoppingListItem> itemList;
  private LayoutInflater inflater;
  private ItemClickCallback itemClickCallback;
  private ShoppingListItem shoppingListItem;
  private ShoppingListItemRepo shoppingListItemRepo;
  private ItemRepo itemRepo;
  private Item item;

  /**
   * Adapter is getting populated with an arrayList of shoppingListItems.
   *
   * @param items shoppingListItems
   * @param c Originating activity context
   */
  public ShoppingListAdapter(ArrayList<ShoppingListItem> items, Context c) {
    inflater = LayoutInflater.from(c);
    this.itemList = items;
  }

  /**
   * Makes the layout with cardview and ShoppingListHolder
   * The holder includes buttons and text for the according item.
   *
   * @param parent N/A
   * @param viewType N/A
   * @return holder layout for buttons and text
   */
  @Override
  public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.shopping_list_card_item, parent, false);
    return new ShoppingListHolder(view);
  }

  /**
   * Populates the cardview with text (item name and amount) and the checked-mark if the item was already bought.
   *
   * @param holder Specifies which item to render. Header, itemRow or Footer.
   * @param position Which Row of the list will be rendered.
   */
  @Override
  public void onBindViewHolder(ShoppingListHolder holder, final int position) {
    shoppingListItem = itemList.get(position);
    shoppingListItemRepo = new ShoppingListItemRepo();
    itemRepo = new ItemRepo();
    item = itemRepo.findOneByID(shoppingListItem.getItemid());
    holder.name.setText(item.getName());
    double itemAmount = shoppingListItem.getAmount() + shoppingListItem.getAmountFromRecipes();
    if (itemAmount % 1 == 0) {
      holder.amount.setText(Integer.toString((int) itemAmount) + " " + item.getUnit());
    } else {
      holder.amount.setText(Double.toString((double) Math.round(itemAmount * 10) / (double) 10) + " " + item.getUnit());
    }
    if (shoppingListItem.getBought() == 1) {
      holder.checkBox.setChecked(true);
    } else {
      holder.checkBox.setChecked(false);
    }
  }
  
  @Override
  public int getItemCount() {
    if (itemList == null) {
      return 0;
    } else {
      return itemList.size();
    }
    
  }

  /**
   * Increases the amount of the specific shoppinglistitem. shoppinglistitem will be updated in the list and in the DB.
   *
   * @param item shoppinglistitem on which the amount should be increased.
   * @param increase certain number for increasing the amount of the specific shoppinglistiem.
   */
  public void addAmount(ShoppingListItem item, double increase){
    item.setAmount(item.getAmount() + increase);
    shoppingListItemRepo.update(item);
    itemList = shoppingListItemRepo.getNotDeleted();
    setItemList(itemList);
  }

  /**
   * Decreases  the amount of the specific shoppinglistitem. shoppinglistitem will be updated in the list and in the DB.
   *
   * @param item shoppinglistitem on which the amount should be decreased.
   * @param decrease certain number for decreasing the amount of the specific shoppinglistiem.
   */
  public void removeAmount(ShoppingListItem item, double decrease){
    item.setAmount(item.getAmount() + decrease);
    shoppingListItemRepo.update(item);
    itemList = shoppingListItemRepo.getNotDeleted();
    setItemList(itemList);
  }


  /**
   * Inserts or removes the checkmark on the specific item
   * If all items are checked ,the list will be emptied.
   * DB will be updated appropriately.
   *
   * @param item shoppinglistitem
   */
  public void changeBought(ShoppingListItem item){
    int i = 0;
    item.setBought(item.getBought() == 1 ? 0 : 1);
    if(item.getDatebought()==null){
      item.setDatebought(new LocalDateTime());
    }else{
      item.setDatebought(null);
    }
    shoppingListItemRepo.update(item);
    for (ShoppingListItem shoppingListItem : itemList) {
      if (shoppingListItem.getBought() == 1) {
        i = i + 1;
      }
    }
    if (i == itemList.size()) {
      for (ShoppingListItem shoppingListItem : itemList) {
        shoppingListItemRepo.setDeleteFlag(shoppingListItem.getShoppinglistitemID());
      }
      itemList.clear();
      setItemList(itemList);
    }
  }

  /**
   * New shoppinglistitem will be added to the list and the DB. Not only will
   * a shoppinglisitem-object created, but also an item-object, if it doesn't
   * exist yet.
   *
   * @param name itemname
   * @param amount item amount
   * @param unit  g,kg, ml, l, Stücke
   */
  public void addItemToList(String name, double amount, String unit){
    ItemRepo itemRepo = new ItemRepo();
    Item item = new Item(itemRepo.findAll().size() + 1, name, amount, unit);
    item = itemRepo.safeInsert(item);

    ShoppingListItemRepo shoppingListItemsRepo = new ShoppingListItemRepo();
    ShoppingListItem shoppingListitem =
            new ShoppingListItem(2, item.getItemID(), amount, 0, null, new LocalDateTime(), 1, 0);
    shoppingListItemsRepo.insert(shoppingListitem);

    itemList = shoppingListItemsRepo.getNotDeleted();
    setItemList(itemList);
  }

  /**
   * Specific shoppinglistitem will be deleted from the list. In the DB a flag will be set on
   * the shoppinglistitem.
   *
   * @param position position of the shoppingListItem in the arrayList
   */
  public void deleteItemFromList(int position){
    ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
    shoppingListItemRepo.setDeleteFlag(itemList.get(position).getShoppinglistitemID());
    shoppingListItemRepo.delete(itemList.get(position).getShoppinglistitemID());
    itemList.remove(position);
    setItemList(itemList);
  }
  
  public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
    this.itemClickCallback = itemClickCallback;
  }
  
  public void setItemList(ArrayList<ShoppingListItem> items) {
    itemList = new ArrayList<>();
    itemList.addAll(items);
    notifyDataSetChanged();
  }

  public ArrayList<ShoppingListItem> getItemList(){
    return itemList;
  }

  
  public interface ItemClickCallback {
    void addAmount(int adapterPosition);

    void removeAmount(int adapterPosition);

    void changeBought(int adapterPosition);

    void showFullName(int adapterPosition);

  }

  /**
   * Holder class which defines the layout of the cardview.
   * The holder also sets listeners on buttons, checkBox and textView.
   */
  class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CheckBox checkBox;
    private Button plus;
    private Button minus;
    private TextView name;
    private TextView amount;
    private Spinner unit;
    
    public ShoppingListHolder(View itemView) {
      super(itemView);
      checkBox = (CheckBox) itemView.findViewById(R.id.checkBox_item);
      plus = (Button) itemView.findViewById(R.id.button_plus);
      minus = (Button) itemView.findViewById(R.id.button_minus);
      name = (TextView) itemView.findViewById(R.id.textView_item_name);
      amount = (TextView) itemView.findViewById(R.id.edittext_amount);
      plus.setOnClickListener(this);
      minus.setOnClickListener(this);
      checkBox.setOnClickListener(this);
      name.setOnClickListener(this);
    }

    /**
     * Handels onClick behaviour.
     * @param v The caller view.
     */
    @Override
    public void onClick(View v) {
      int i = v.getId();
      int j = R.id.button_plus;
      if (v.getId() == R.id.button_plus) {
        itemClickCallback.addAmount(getAdapterPosition());
        System.out.println("plus button tapped");
      } else if (v.getId() == R.id.button_minus) {
        itemClickCallback.removeAmount(getAdapterPosition());
        System.out.println("minus button tapped");
      } else if (v.getId() == R.id.checkBox_item) {
        itemClickCallback.changeBought(getAdapterPosition());
      } else if (v.getId() == R.id.textView_item_name) {
        itemClickCallback.showFullName(getAdapterPosition());
      }
      
      System.out.println("item was clicked " + v.getId());
    }
  }
}
