package ch.zhaw.it15a_zh.psit3_03.mealmanager.models;

/**
 * Domain data class for an item.
 */
public class Item {
  private int itemID;
  private String name;
  private double amount;
  private String unit;
  
  public Item(int itemID, String name, double amount, String unit) {
    this.name = name;
    this.amount = amount;
    this.unit = unit;
    this.itemID = itemID;
  }
  
  public Item() {
  }
  
  public int getItemID() {
    return itemID;
  }
  
  public void setItemID(int itemID) {
    this.itemID = itemID;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public double getAmount() {
    return amount;
  }
  
  public void setAmount(double amount) {
    this.amount = amount;
  }
  
  public String getUnit() {
    return unit;
  }
  
  public void setUnit(String unit) {
    this.unit = unit;
  }
  
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Item)) {
      return false;
    }
    Item other = (Item) object;
    return (this.getItemID() == other.getItemID() && this.getName().equals(other.getName()) &&
        this.getUnit().equals(other.getUnit()));
  }
  
  @Override
  public String toString() {
    return "Item{" + "itemID=" + itemID + ", name='" + name + '\'' + ", amount=" + amount + ", unit='" + unit + '\'' +
        '}';
  }
}

