package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

import org.joda.time.LocalDateTime;
/**
 * Domain class for a shoppinglistitem.
 */
public class ShoppingListItem {
  private int shoppinglistid;
  private int itemid;
  private double amount;
  private int bought;
  private LocalDateTime datebought;
  private LocalDateTime dateadded;
  private int listgroupid;
  private int deleted;
  private int shoppinglistitemID;
  private double amountFromRecipes;

  public ShoppingListItem() {

  }

  public ShoppingListItem(int shoppinglistid, int itemid, double amount, int bought, LocalDateTime datebought,
                          LocalDateTime dateadded, int listgroupid, double amountFromRecipes) {
    this.shoppinglistid = shoppinglistid;
    this.itemid = itemid;
    this.amount = amount;
    this.bought = bought;
    this.datebought = datebought;
    this.dateadded = dateadded;
    this.listgroupid = listgroupid;
    this.deleted = 0;
    this.amountFromRecipes = amountFromRecipes;
  }

  public int getShoppinglistid() {
    return shoppinglistid;
  }

  public void setShoppinglistid(int shoppinglistid) {
    this.shoppinglistid = shoppinglistid;
  }

  public int getItemid() {
    return itemid;
  }

  public void setItemid(int itemid) {
    this.itemid = itemid;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public int getBought() {
    return bought;
  }

  public void setBought(int bought) {
    this.bought = bought;
  }

  public LocalDateTime getDatebought() {
    return datebought;
  }

  public void setDatebought(LocalDateTime datebought) {
    this.datebought = datebought;
  }

  public LocalDateTime getDateadded() {
    return dateadded;
  }

  public void setDateadded(LocalDateTime dateadded) {
    this.dateadded = dateadded;
  }

  public int getListgroupid() {
    return listgroupid;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public int getShoppinglistitemID() {
    return shoppinglistitemID;
  }

  public void setShoppinglistitemID(int shoppinglistitemID) {
    this.shoppinglistitemID = shoppinglistitemID;
  }

  /**
   * Compare two shoppinglistitems.
   * If shoppinglistitemID, itemID, shoppinglistID and Listgroupid are the same, then they are equal.
   *
   * @param object The other object of ShoppingListItem.
   * @return true, if they are equal, else false.
   */
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof ShoppingListItem)) {
      return false;
    }
    ShoppingListItem other = (ShoppingListItem) object;
    return (this.getShoppinglistitemID() == other.getShoppinglistitemID() && this.getItemid() == other.getItemid() &&
            this.getShoppinglistid() == other.getShoppinglistid() && this.getListgroupid() == other.getListgroupid());
  }

  public double getAmountFromRecipes() {
    return amountFromRecipes;
  }

  public void setAmountFromRecipes(double amountFromRecipes) {
    this.amountFromRecipes = amountFromRecipes;
  }

  @Override
  public String toString() {
    return "ShoppingListItem{" + "shoppinglistid=" + shoppinglistid + ", itemid=" + itemid + ", amount=" + amount +
            ", bought=" + bought + ", datebought=" + datebought + ", dateadded=" + dateadded + ", listgroupid=" +
            listgroupid + ", deleted=" + deleted + ", shoppinglistitemID=" + shoppinglistitemID + ", amountFromRecipes=" +
            amountFromRecipes + '}';
  }
}
