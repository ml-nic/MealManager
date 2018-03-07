package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.squareup.timessquare.CalendarPickerView;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;

/**
 * Activity which allows the user to select a date range to plan recipes for. Today's date is preselected
 */
public class DateRangePicker extends AppCompatActivity {
  private List<Date> selectedDates;
  
  /**
   * Handler-Class for Logic and UI
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    
    View view = new View(getApplicationContext());
    view.isInEditMode();
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
    setContentView(R.layout.activity_date_range_picker);
    /* BEGIN TOOLBAR */
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weekplan_overview);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.toolbar_title_weekplan_activity);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    /* END TOOLBAR */
    
    Calendar nextYear = Calendar.getInstance();
    nextYear.add(Calendar.YEAR, 1);
    
    final CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
    Date today = new Date();
    calendar.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
    
    Button startPlanning = (Button) findViewById(R.id.button_image_view_start_planning);
    startPlanning.setOnClickListener(setButtonListenerAddDatesToPlanning(calendar));
  }
  
  private Button.OnClickListener setButtonListenerAddDatesToPlanning(final CalendarPickerView calendar) {
    return new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectedDates = calendar.getSelectedDates();
        String[] selectedDatesString = new String[selectedDates.size()];
        for (int i = 0; i < selectedDates.size(); i++) {
          Date selectedDate = selectedDates.get(i);
          String date = selectedDate.toString();

          date = date.substring(0, 11) + date.substring(date.length() - 4, date.length());
          selectedDatesString[i] = date;
        }
        //Send the StringArray back to the WeekPlan activity
        setBundleAndStartWeekplanActivity(selectedDatesString);
      }
    };
  }
  
  private void setBundleAndStartWeekplanActivity(String[] selectedDatesString) {
    Bundle bundle = new Bundle();
    bundle.putStringArray("selectedDatesString", selectedDatesString);
    Intent intent = new Intent(getApplicationContext(), WeekPlanActivity.class);
    intent.putExtras(bundle);
    startActivity(intent);
  }
  
}
