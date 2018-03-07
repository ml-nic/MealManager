package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.FilterAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.RecipeOverviewAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.SortAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;

/**
 * Handles the behaviour of RecipeOverviewActivity
 * Starts the activity and loads the layout
 */
public class RecipeOverviewActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private static RecyclerView recyclerViewFilterAllKinds, recyclerViewFilterAllMeals;
  private static TextView textViewFilterArtFinishedKind, textViewFilterArtFinishedMeal;
  private static DrawerLayout drawer;
  private List<Recipe> recipeList = new ArrayList<>();
  private RecipeOverviewAdapter recipeOverviewAdapter;
  private FilterAdapter filterAdapterAllMeals, filterAdapterAllKinds;
  
  /**
   * Hides the filter recyclerviews in the navigation drawer.
   */
  public static void hideFilterList() {
    recyclerViewFilterAllKinds.setVisibility(View.GONE);
    textViewFilterArtFinishedKind.setVisibility(View.GONE);
    
    recyclerViewFilterAllMeals.setVisibility(View.GONE);
    textViewFilterArtFinishedMeal.setVisibility(View.GONE);
  }
  
  /**
   * Close the Navigation Drawer
   */
  public static void closeDrawer() {
    drawer.closeDrawer(GravityCompat.START);
  }
  
  /**
   * Creates the Activity and renders the layout.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_recipe_overview);
    
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.recipe_overview_title);
    
    String dateHeader;
    if (this.getIntent().getExtras() != null) {
      Bundle bundle = this.getIntent().getExtras();
      dateHeader = bundle.getString("dateHeader");
      Log.e("DateHeader: ", String.valueOf(dateHeader));
    } else {
      dateHeader = null;
    }
    
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    
    prepareRecipeData();
    
    setUpRecyclerviews(dateHeader);
    setUpTextViews();
  }
  
  /**
   * Setting up the different TextViews and handle them.
   */
  private void setUpTextViews() {
    final View filterRowOne = findViewById(R.id.filter_row_one);
    final View filterRowTwo = findViewById(R.id.filter_row_two);
    
    final TextView textViewFilterArtKind = (TextView) filterRowOne.findViewById(R.id.textView_filter_art);
    textViewFilterArtFinishedKind = (TextView) filterRowOne.findViewById(R.id.textView_filter_art_finished);
    
    textViewFilterArtKind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recyclerViewFilterAllKinds.setVisibility(View.VISIBLE);
        textViewFilterArtFinishedKind.setVisibility(View.VISIBLE);
      }
    });
    textViewFilterArtFinishedKind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recyclerViewFilterAllKinds.setVisibility(View.GONE);
        textViewFilterArtFinishedKind.setVisibility(View.GONE);
      }
    });
    
    TextView textViewFilterArtMeal = (TextView) filterRowTwo.findViewById(R.id.textView_filter_art);
    textViewFilterArtMeal.setText(R.string.all_courses);
    textViewFilterArtFinishedMeal = (TextView) filterRowTwo.findViewById(R.id.textView_filter_art_finished);
    textViewFilterArtMeal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recyclerViewFilterAllMeals.setVisibility(View.VISIBLE);
        textViewFilterArtFinishedMeal.setVisibility(View.VISIBLE);
      }
    });
    textViewFilterArtFinishedMeal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recyclerViewFilterAllMeals.setVisibility(View.GONE);
        textViewFilterArtFinishedMeal.setVisibility(View.GONE);
      }
    });
    
    TextView textViewResetSort = (TextView) findViewById(R.id.textView_reset_sort);
    textViewResetSort.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recipeOverviewAdapter.resetSort();
        closeDrawer();
      }
    });
    TextView textViewResetFilter = (TextView) findViewById(R.id.textView_reset_filter);
    textViewResetFilter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        recipeOverviewAdapter.resetFilter();
        filterAdapterAllMeals.clearOtherImages(-1);
        filterAdapterAllKinds.clearOtherImages(-1);
        closeDrawer();
      }
    });
  }
  
  /**
   * Setting up the different Recyclerviews.
   *
   * @param dateHeader Header for different behaviour of RecipeOverviewAdapter
   */
  private void setUpRecyclerviews(String dateHeader) {
    //Create RecyclerView/Adapter and displays them on the layout
    RecyclerView recyclerViewRecipes = (RecyclerView) findViewById(R.id.recycler_view_recipes);
    recipeOverviewAdapter = new RecipeOverviewAdapter(RecipeOverviewActivity.this, recipeList, dateHeader);
    RecyclerView.LayoutManager layoutManagerRecipeOverview = new LinearLayoutManager(getApplicationContext());
    recyclerViewRecipes.setLayoutManager(layoutManagerRecipeOverview);
    recyclerViewRecipes.setItemAnimator(new DefaultItemAnimator());
    recyclerViewRecipes.setAdapter(recipeOverviewAdapter);
    
    //Create RecyclerView/Adapter and displays them on the layout
    RecyclerView recyclerViewSort = (RecyclerView) findViewById(R.id.recycler_view_sort);
    SortAdapter sortAdapter = new SortAdapter(RecipeOverviewActivity.this, recipeOverviewAdapter);
    RecyclerView.LayoutManager layoutManagerSort = new LinearLayoutManager(getApplicationContext());
    recyclerViewSort.setLayoutManager(layoutManagerSort);
    recyclerViewSort.setItemAnimator(new DefaultItemAnimator());
    recyclerViewSort.setAdapter(sortAdapter);
    
    ArrayList<String> filterAllKindsList = new ArrayList<>();
    filterAllKindsList.add("Fleisch");
    filterAllKindsList.add("Gemüse");
    filterAllKindsList.add("Vegan");
    filterAllKindsList.add("Salat");
    
    recyclerViewFilterAllKinds = (RecyclerView) findViewById(R.id.recycler_view_filter_all_kinds);
    filterAdapterAllKinds = new FilterAdapter(filterAllKindsList, recipeOverviewAdapter, "type");
    RecyclerView.LayoutManager layoutManagerFilterAllKinds = new LinearLayoutManager(getApplicationContext());
    recyclerViewFilterAllKinds.setLayoutManager(layoutManagerFilterAllKinds);
    recyclerViewFilterAllKinds.setItemAnimator(new DefaultItemAnimator());
    recyclerViewFilterAllKinds.setAdapter(filterAdapterAllKinds);
    
    ArrayList<String> filterAllMealsList = new ArrayList<>();
    filterAllMealsList.add("Frühstück");
    filterAllMealsList.add("Mittagessen");
    filterAllMealsList.add("Abendessen");
    filterAllMealsList.add("Snack");
    
    recyclerViewFilterAllMeals = (RecyclerView) findViewById(R.id.recycler_view_filter_all_meals);
    filterAdapterAllMeals = new FilterAdapter(filterAllMealsList, recipeOverviewAdapter, "kind");
    RecyclerView.LayoutManager layoutManagerFilterAllMeals = new LinearLayoutManager(getApplicationContext());
    recyclerViewFilterAllMeals.setLayoutManager(layoutManagerFilterAllMeals);
    recyclerViewFilterAllMeals.setItemAnimator(new DefaultItemAnimator());
    recyclerViewFilterAllMeals.setAdapter(filterAdapterAllMeals);
  }
  
  private void prepareRecipeData() {
    RecipeRepo recipeRepo = new RecipeRepo();
    recipeList = recipeRepo.findAll();
  }
  
  /**
   * Not used, because of different ItemSelect handling.
   *
   * @param item The selected MenuItem
   *
   * @return always false, because of different ItemSelect handling.
   */
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    return false;
  }
}