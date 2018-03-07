package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A custom SpinnerAdapter for preparing the units (g, kg, ml, l, Stücke) for
 * dispalying them in a window, in which the user can choose between
 * the units.
 */
public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
  private final Context activity;
  private ArrayList<String> asr;

  /**
   * Creating a customAdapter
   *
   * @param context Originating activity context
   * @param asr The units are given to the adapter as a list of Strings.
   */
  public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
    this.asr = asr;
    activity = context;
  }

  public int getCount() {
    return asr.size();
  }

  public Object getItem(int i) {
    return asr.get(i);
  }

  public long getItemId(int i) {
    return (long) i;
  }

  /**
   * Designing unit-entry in the dropdownview.
   *
   * @param position position of the specific unit
   * @param convertView N/A
   * @param parent N/A
   * @return adjusted dropdownView
   */
  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    TextView txt = new TextView(activity);
    txt.setPadding(16, 16, 16, 16);
    txt.setTextSize(30);
    txt.setGravity(Gravity.CENTER);
    txt.setText(asr.get(position));
    txt.setTextColor(Color.parseColor("#FFFFFF"));
    return txt;
  }

  /**
   * Additional Designings for the dropdownview
   *
   * @param position position of the item to be designed
   * @param view The caller view
   * @param viewgroup The caller viewGroup
   * @return designed item
   */
  public View getView(int position, View view, ViewGroup viewgroup) {
    TextView txt = new TextView(activity);
    txt.setGravity(Gravity.CENTER);
    txt.setPadding(16, 16, 16, 16);
    txt.setTextSize(16);
    txt.setText(asr.get(position));
    txt.setTextColor(Color.parseColor("#FFFFFF"));
    return txt;
  }

}
