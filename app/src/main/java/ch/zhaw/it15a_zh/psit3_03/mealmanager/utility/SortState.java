package ch.zhaw.it15a_zh.psit3_03.mealmanager.utility;

/**
 * Enum for the three different sortstates.
 * Ascending, descending and none.
 */
public enum SortState {
  ASCENDING, DESCENDING, NONE;
  private static SortState[] vals = values();

  /**
   * Gets the next SortState.
   *
   * @return The next SortState.
   */
  public SortState next() {
    return vals[(this.ordinal() + 1) % vals.length];
  }
}
