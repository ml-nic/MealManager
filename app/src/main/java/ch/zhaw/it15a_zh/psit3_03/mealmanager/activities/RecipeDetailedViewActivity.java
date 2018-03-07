package ch.zhaw.it15a_zh.psit3_03.mealmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.RecipeDetailedViewAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeItemsRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.UserPlannedRecipesRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.UserPlannedRecipe;

/**
 * Activity responsible for displaying the detailed view of an Recipe
 */
public class RecipeDetailedViewActivity extends AppCompatActivity {
  private static boolean notInplanningMode = true;
  private final RecipeRepo recipeRepo = new RecipeRepo();
  private final UserPlannedRecipesRepo uprr = new UserPlannedRecipesRepo();
  private UserPlannedRecipe upr = new UserPlannedRecipe();
  private int recipeID;
  private String dateHeader;
  
  /**
   * When Pressing the Phone's Back-Button, the app goes back to the home screen instead of the last activity
   */
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    this.finish();
  }
  
  /**
   * This method is responsible for managing the logistics of the detailed recipe view. Depending on weather the
   * Activity has been launched via the Weekplan Activity or view the Recipe Overview Activity, the functionality and
   * labeling of the button will change.
   *
   */
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    //Hide Top Toolbar form android device
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_recipe_detailed);
    
    getBudleData();
    
    /* BEGIN TOOLBAR */
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recipe_detailed_view);
    setSupportActionBar(toolbar);
    String recipeName = recipeRepo.findOneByID(recipeID).getName();
    getSupportActionBar().setTitle(recipeName);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    /* END TOOLBAR */
    
    RecipeItemsRepo rtr = new RecipeItemsRepo();
    List<RecipeItem> recipeItemList = rtr.getRecipeItemBelongingToSpecificRecipe(recipeID);

    /*Begin Recyclerview for list of recipeItems*/
    recyclerViewManager((List<RecipeItem>) recipeItemList);
    /*End Recyclerview for list of recipeItems*/
    Button addItems = (Button) findViewById(R.id.button_image_view_add_items);
    if (notInplanningMode) {
      addItems.setText(R.string.add_items_to_shoppinglist);
      addItems.setOnClickListener(setAddItemsButtonListenerNotInPlanning());
      
    } else {
      addItems.setText(R.string.add_to_planning);
      addItems.setOnClickListener(setAddItemsButtonListenerInPlanning());
      
    }
  }
  
  private void recyclerViewManager(List<RecipeItem> recipeItemList) {
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_item_list);
    RecipeDetailedViewAdapter recipeDetailedViewAdapter = new RecipeDetailedViewAdapter(this, recipeItemList, recipeID);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    setRecyclerOptions(recyclerView, recipeDetailedViewAdapter, mLayoutManager);
  }
  
  private View.OnClickListener setAddItemsButtonListenerInPlanning() {
    
    if (!uprr.returnTrueIfRecipeIsNotPlannedAtDate(dateHeader, recipeID)) {
      return new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Toast.makeText(RecipeDetailedViewActivity.this, "Bereits geplant", Toast.LENGTH_SHORT).show();
        }
        
      };
    } else {
      return new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Toast.makeText(RecipeDetailedViewActivity.this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
          upr = new UserPlannedRecipe();
          upr.setPlannedServings(4);
          upr.setDatePlanned(dateHeader);
          upr.setRecipeID(recipeID);
          uprr.insertUserPlannedRecipe(upr);
          
          ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
          RecipeItemsRepo recipeItemsRepo = new RecipeItemsRepo();
          List<RecipeItem> recipeItemList = recipeItemsRepo.getRecipeItemBelongingToSpecificRecipe(recipeID);
          List<ShoppingListItem> shoppinglistitemList = new ArrayList<>();
          insertItemsToShoppingList(shoppingListItemRepo, recipeItemList, shoppinglistitemList);
          
          Intent intent = new Intent(getApplicationContext(), WeekPlanActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          getApplicationContext().startActivity(intent);
          
        }
      };
    }
  }
  
  private void insertItemsToShoppingList(ShoppingListItemRepo shoppingListItemRepo, List<RecipeItem> recipeItemList,
      List<ShoppingListItem> shoppinglistitemList) {
    for (RecipeItem recipeItem : recipeItemList) {
      ShoppingListItem shoppinglistitem = new ShoppingListItem();
      shoppinglistitem.setAmountFromRecipes(recipeItem.getAmount());
      shoppinglistitem.setBought(0);
      shoppinglistitem.setDateadded(LocalDateTime.now());
      shoppinglistitem.setDatebought(null);
      shoppinglistitem.setDeleted(0);
      shoppinglistitem.setItemid(recipeItem.getItemID());
      shoppinglistitem.setShoppinglistid(1);
      shoppinglistitemList.add(shoppinglistitem);
    }
    
    for (ShoppingListItem shoppinglistitem : shoppinglistitemList) {
      shoppingListItemRepo.insert(shoppinglistitem);
    }
  }
  
  private Button.OnClickListener setAddItemsButtonListenerNotInPlanning() {
    return new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        //TODO Add funtionality to add recipe items and volumes to shoppinglist
        String itemsAddedConfirmation = getResources().getString(R.string.itemsAddedConfirmation);
        Toast.makeText(RecipeDetailedViewActivity.this, itemsAddedConfirmation, Toast.LENGTH_SHORT).show();
  
        ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
        RecipeItemsRepo recipeItemsRepo = new RecipeItemsRepo();
        List<RecipeItem> recipeItemList = recipeItemsRepo.getRecipeItemBelongingToSpecificRecipe(recipeID);
        List<ShoppingListItem> shoppinglistitemList = new ArrayList<>();
  
  
        for (RecipeItem recipeItem : recipeItemList) {
          ShoppingListItem shoppinglistitem = new ShoppingListItem();
          shoppinglistitem.setAmount(recipeItem.getAmount());
          shoppinglistitem.setBought(0);
          shoppinglistitem.setDateadded(LocalDateTime.now());
          shoppinglistitem.setDatebought(null);
          shoppinglistitem.setDeleted(0);
          shoppinglistitem.setItemid(recipeItem.getItemID());
          shoppinglistitem.setShoppinglistid(1);
          shoppinglistitemList.add(shoppinglistitem);
        }
        
        for(ShoppingListItem shoppingListItem : shoppinglistitemList){
          shoppingListItemRepo.insert(shoppingListItem);
        }
        
        
      }
    };
  }
  
  private void setRecyclerOptions(RecyclerView recyclerView, RecipeDetailedViewAdapter recipeDetailedViewAdapter,
      RecyclerView.LayoutManager mLayoutManager) {
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setEnabled(false);
    recyclerView.setAdapter(recipeDetailedViewAdapter);
  }
  
  private void getBudleData() {
    if (this.getIntent().getExtras() != null) {
      Intent intent = getIntent();
      Bundle bundle = intent.getExtras();
      recipeID = bundle.getInt("recipeid");
      if (bundle.getString("dateHeader") != null) {
        dateHeader = bundle.getString("dateHeader");
        Log.e("DateHeader: ", dateHeader);
        notInplanningMode = false;
      } else {
        notInplanningMode = true;
      }
    }
  }
}
