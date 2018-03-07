package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.MainActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.OuterWeekplanAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.PlannedRecipeRepo;

/**
 * This Activity is responsible for displaying the weekplan.
 * In order to show all dates, and group planned recipes by date, I have decided to go for a nested RecyclerView
 * layout as follows. WeekPlanLayout > Outer RecyclerView > CardView > InnerRecyclerView
 * ">" means 'Contains' in this context. The CardView is never explicitly instantiated as Android doesn't require this.
 */
public class WeekPlanActivity extends AppCompatActivity {
  private PlannedRecipeRepo prr = new PlannedRecipeRepo();
  private List<String> selectedDatesString = new ArrayList<>();
  private List<DateTime> localDatesUnformatted = new ArrayList<>();
  private List<String> distinctUserPlannedDates = new ArrayList<>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    //Hide Top Toolbar form android device
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    //Set LayoutFile
    setContentView(R.layout.activity_weekplan);
    
    /* BEGIN TOOLBAR */
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weekplan_overview);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.toolbar_title_weekplan_activity);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    /* END TOOLBAR */
    
    //Get Selected Dates from DatePicker activity, if dates have been selected
    getBundleData();
    
    //Get dates for which user has already planned recipes
    distinctUserPlannedDates = prr.getAllDatesPlanned();
    
    //See if user wanted to add new dates, compare with already planned, and add difference.
    comparePlannedDatesToNewDates(distinctUserPlannedDates);
    
    //Sort Dates to display
    sortDates();
    
    //If there are new dates, insert these new dates into Database
    insertNewDates();
    
    
    /*Begin OuterRecyclerView*/
    RecyclerView outerRecyclerView = (RecyclerView) findViewById(R.id.activity_weekplan_outer_recyclerview);
    OuterWeekplanAdapter outerWeekplanAdapter =
        new OuterWeekplanAdapter(getApplicationContext(), distinctUserPlannedDates);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
    setRecyclerviewOptions(outerRecyclerView, outerWeekplanAdapter, manager);
    /*End OuterRecyclerView*/
  }
  
  /**
   * This is a bit of a round-about way of sorting the dates chronologically. Android does not support Java 8 Time
   * Features, so JodaTime has to be used. The dates are stored in the database as Strings (in hindsight, perhaps not
   * my best idea), are converted to a DateTime Object with the help of the DateTimeFormatter, added to a Collection
   * and sorted, then converted back to strings in the original format and passed to the RecyclerView.
   *
   * It might not be pretty but it doesn't break.
   */
  private void sortDates() {
    DateTimeFormatter dbDateFormatToDateTimeFormat = DateTimeFormat.forPattern("EEE MMM dd yyyy");
    for (String distinctUserPlannedDate : distinctUserPlannedDates) {
      DateTime dateTime = DateTime.parse(distinctUserPlannedDate, dbDateFormatToDateTimeFormat);
      localDatesUnformatted.add(dateTime);
    }
    Collections.sort(localDatesUnformatted);
    distinctUserPlannedDates.clear();
    for (DateTime aLocalDatesUnformatted : localDatesUnformatted) {
      String unformattedDate = aLocalDatesUnformatted.toString(dbDateFormatToDateTimeFormat);
      distinctUserPlannedDates.add(unformattedDate);
    }
    localDatesUnformatted.clear();
    
  }
  
  private void setRecyclerviewOptions(RecyclerView outerRecyclerView, OuterWeekplanAdapter outerWeekplanAdapter,
      RecyclerView.LayoutManager manager) {
    outerRecyclerView.setLayoutManager(manager);
    outerRecyclerView.setItemAnimator(new DefaultItemAnimator());
    outerRecyclerView.setAdapter(outerWeekplanAdapter);
  }
  
  private void insertNewDates() {
    if (selectedDatesString.size() > 0) {
      prr.insertListPlannedDates(selectedDatesString);
    }
    selectedDatesString.clear();
  }
  
  private void comparePlannedDatesToNewDates(List<String> distinctUserPlannedDates) {
    try {
      if (selectedDatesString.size() > 0) {
        for (String distinctUserPlannedDate : distinctUserPlannedDates) {
          for (int e = 0; e < selectedDatesString.size(); e++) {
            if (distinctUserPlannedDate.contains(selectedDatesString.get(e))) {
              selectedDatesString.remove(e);
            }
          }
        }
        //Add dates difference to distingUserDates for the RecyclerView to work with
        for (String aSelectedDatesString : selectedDatesString) {
          distinctUserPlannedDates.add(aSelectedDatesString);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void getBundleData() {
    if (this.getIntent().getExtras() != null) {
      Bundle b = this.getIntent().getExtras();
      String[] dateRangePickerSelectedDates = b.getStringArray("selectedDatesString");
      Collections.addAll(selectedDatesString, dateRangePickerSelectedDates);
    }
  }
  
  //Inflate Options Menu to show additional buttons in toolbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_weekplan_main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  //Launches DatePicker activity
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.ic_action_weekplan_add:
        Intent intent = new Intent(this, DateRangePicker.class);
        Context context = this;
        context.startActivity(intent);
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, MainActivity.class);
    Context context = this;
    context.startActivity(intent);
    
    super.onBackPressed();
  }
}