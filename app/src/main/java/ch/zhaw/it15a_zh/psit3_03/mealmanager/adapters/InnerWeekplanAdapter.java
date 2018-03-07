package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeDetailedViewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeOverviewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeItemsRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.UserPlannedRecipesRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.UserPlannedRecipe;

/**
 * The InnerWeekplanAdapter handles the layout of an individual group of recipes, which are grouped by Date
 */
class InnerWeekplanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int TYPE_HEADER = 0;
  private static final int TYPE_ITEM = 1;
  private static final int TYPE_FOOTER = 2;
  private final List<UserPlannedRecipe> userPlannedRecipeList;
  private final UserPlannedRecipesRepo uprr = new UserPlannedRecipesRepo();
  private final Context context;
  private final String dateHeader;
  private int newPosition;
  
  /**
   * Adapter which handles populating individual rows of a CardView with content
   *
   * @param dateHeader Date for which the recipes will be grouped by
   * @param userPlannedRecipeList List of recipes which have been planned for that specific day (dateHeader)
   * @param context Originating activity context
   */
  public InnerWeekplanAdapter(String dateHeader, List<UserPlannedRecipe> userPlannedRecipeList, Context context) {
    this.userPlannedRecipeList = userPlannedRecipeList;
    this.context = context;
    this.dateHeader = dateHeader;
  }
  
  /**
   * Makes the layouts header, planned recipe and footer available to the onBind
   *
   * @param parent N/A
   * @param viewType either Header, plannedRecipe or Footer
   *
   * @return View of either Header, plannedRecipe or Footer
   */
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_HEADER) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weeplan_day_header, parent, false);
      return new WeekplanHeaderViewHolder(v);
    } else if (viewType == TYPE_FOOTER) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weekplan_day_footer, parent, false);
      return new WeekplanFooterViewHolder(v);
    } else if (viewType == TYPE_ITEM) {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.activity_weekplan_day_planned_recipe, parent, false);
      return new WeekplanItemViewHolder(v);
    }
    return null;
  }
  
  /**
   * Populates the header (Date), itemRows (planned recipes)(if they exsist) and footer(add recipes to a day) with
   * the appropriate information from the mmdb database combined with the dates the user selected in the datepicker
   * activity, if he picked any.
   *
   * @param holder Specifies which item to render. Header, itemRow or Footer.
   * @param position Which Row of the list will be rendered.
   */
  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    //Implement views if recipes have been planned, otherwise only header and footer will be displayed
    //if (userPlannedrecipeList.Size() > 0 is true, recipes have been planned and need to be displayed.
    if (userPlannedRecipeList.size() > 0) {
      if (holder instanceof WeekplanHeaderViewHolder) {
        setWeekplanHeader((WeekplanHeaderViewHolder) holder);
      } else if (holder instanceof WeekplanItemViewHolder) {
        setWeekplanItemListIfRecipes(holder, position);
        
      } else {
        setWeekplanFooterIfRecipes((WeekplanFooterViewHolder) holder);
      }
      // This case renders the layout if no recipes have been planned for a given day yet. It will only render the
      // header and the footer.
    } else {
      if (holder instanceof WeekplanHeaderViewHolder) {
        setWeekplanHeader((WeekplanHeaderViewHolder) holder);
        
      } else if (holder instanceof WeekplanItemViewHolder) {
        WeekplanItemViewHolder weekplanItemViewHolder = (WeekplanItemViewHolder) holder;
      } else {
        setWeekplanFooterIfNoRecipes((WeekplanFooterViewHolder) holder);
      }
    }
  }
  
  private void setWeekplanFooterIfNoRecipes(WeekplanFooterViewHolder weekplanFooterViewHolder) {
    weekplanFooterViewHolder.button_plan_recipes_for_this_day.setText(R.string.add_recipe_to_this_day);
    weekplanFooterViewHolder.button_plan_recipes_for_this_day
        .setOnClickListener(addRecipeToDayAndLaunchWeekplanIfPlanEmpty());
  }
  
  private void setWeekplanFooterIfRecipes(WeekplanFooterViewHolder weekplanFooterViewHolder) {
    weekplanFooterViewHolder.button_plan_recipes_for_this_day.setText(R.string.add_recipe_to_this_day);
    
    weekplanFooterViewHolder.button_plan_recipes_for_this_day
        .setOnClickListener(setOnClickListenerAddNewRecipeIfPlanNotEmpty());
  }
  
  private void setWeekplanItemListIfRecipes(RecyclerView.ViewHolder holder, int position) {
    WeekplanItemViewHolder weekplanItemViewHolder = (WeekplanItemViewHolder) holder;
    final Recipe recipe = new RecipeRepo().findOneByID(userPlannedRecipeList.get(position - 1).getRecipeID());
    //Sets Recipe Name
    weekplanItemViewHolder.textView_recipe_name.setText(recipe.getName());
    //Sets Image Thumbnail
    String imageID = recipe.getImage();
    imageID = imageID.substring(0, imageID.lastIndexOf("."));
    Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/" + imageID);
    Picasso.with(context).load(uri).error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
        .into(weekplanItemViewHolder.imageView_thumbnail_recipe_image);
    //Sets Short Recipe Description
    weekplanItemViewHolder.textView_short_recipe_description.setText(recipe.getDescription());
    //Sets Cooking Time
    weekplanItemViewHolder.textView_cookingTime.setText(String.valueOf(recipe.getCookingTime()));
    weekplanItemViewHolder.imageButton_remove_recipe_from_planning
        .setOnClickListener(setOnButtonListenerRemoveRecipeFromPlanning(holder, recipe));
    weekplanItemViewHolder.relativelayout_recipe_container
        .setOnClickListener(setOnClickListenerAddNewRecipeToPlanIfPlanNotEmpty(holder));
  }
  
  private View.OnClickListener setOnClickListenerAddNewRecipeIfPlanNotEmpty() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Add Date to launching Intent
        Intent intent = new Intent(context, RecipeOverviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dateHeader", dateHeader);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      }
    };
  }
  
  private View.OnClickListener setOnClickListenerAddNewRecipeToPlanIfPlanNotEmpty(
      final RecyclerView.ViewHolder holder) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, RecipeDetailedViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("recipeid", userPlannedRecipeList.get(holder.getAdapterPosition() - 1).getRecipeID());
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        
      }
    };
  }
  
  private View.OnClickListener setOnButtonListenerRemoveRecipeFromPlanning(final RecyclerView.ViewHolder holder,
      final Recipe recipe) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Depending on which order the user decideds to remove recipes, the position variable needs adjusting and
        // catching of NullPointerExceptions. It's not the cleanest bit of code but it will prevent the activity
        // from crashing.
        updateListPosition(holder);
        boolean success = false;
        try {
          success = uprr.removePlannedRecipe(userPlannedRecipeList.get(newPosition).getRecipeID(), dateHeader);
         // removeItemsFromShoppingList(recipe);
        } catch (Exception e) {
          e.printStackTrace();
          newPosition = newPosition + 1;
          try {
            success = uprr.removePlannedRecipe(userPlannedRecipeList.get(newPosition).getRecipeID(), dateHeader);
           // if(success) removeItemsFromShoppingList(recipe);
  
          } catch (Exception e1) {
            e1.printStackTrace();
            newPosition = newPosition - 2;
            success = uprr.removePlannedRecipe(userPlannedRecipeList.get(newPosition).getRecipeID(), dateHeader);
          //  if(success) removeItemsFromShoppingList(recipe);
          }
        } finally {
          removeItemsFromShoppingList(recipe);
        }
        //If the removal of the item from the SQLiteDataBase was successfull, remove it from the users interface,
        // otherwise, don't.
        if (success) {
          updateListIfItemRemoved();
        } else {
          toastFailedToRemoveItem(recipe);
        }
      }
    };
  }
  
  private void removeItemsFromShoppingList(Recipe recipe) {
    ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
    RecipeItemsRepo recipeItemsRepo = new RecipeItemsRepo();
    List<RecipeItem> recipeItemList = recipeItemsRepo.getRecipeItemBelongingToSpecificRecipe(recipe.getRecipeID());
    
    for(RecipeItem recipeItem : recipeItemList){
      List<ShoppingListItem> shoppingListItems = shoppingListItemRepo.getShoppinglistitemsByItemID(recipeItem
          .getItemID());
      if (shoppingListItems != null && shoppingListItems.size() > 0){
        ShoppingListItem shoppinglistitem = shoppingListItems.get(0);
        shoppinglistitem.setAmountFromRecipes(shoppinglistitem.getAmountFromRecipes() - recipeItem.getAmount());
        if(shoppinglistitem.getAmountFromRecipes() == 0 && shoppinglistitem.getAmount() == 0) {
          shoppingListItemRepo.delete(shoppinglistitem.getShoppinglistitemID());
        }else {
          shoppingListItemRepo.update(shoppinglistitem);
        }
      }
    }

  }
  
  private void updateListPosition(RecyclerView.ViewHolder holder) {
    if (userPlannedRecipeList.size() == 1) {
      newPosition = holder.getAdapterPosition() - 2;
    } else {
      newPosition = holder.getAdapterPosition() - 1;
    }
  }
  
  private void toastFailedToRemoveItem(Recipe recipe) {
    Toast.makeText(context,
        "FAILED: " + recipe.getName() + "  UPRI " + userPlannedRecipeList.get(newPosition).getRecipeID(),
        Toast.LENGTH_SHORT).show();
  }
  
  private void updateListIfItemRemoved() {
    userPlannedRecipeList.remove(newPosition);
    notifyItemRemoved(newPosition);
    notifyItemRangeChanged(newPosition, userPlannedRecipeList.size());
  }
  
  private View.OnClickListener addRecipeToDayAndLaunchWeekplanIfPlanEmpty() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Add Date to launching Intent
        Intent intent = new Intent(context, RecipeOverviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("dateHeader", dateHeader);
        intent.putExtras(bundle);
        context.getApplicationContext().startActivity(intent);
      }
    };
  }
  
  private void setWeekplanHeader(WeekplanHeaderViewHolder weekplanHeaderViewHolder) {
    weekplanHeaderViewHolder.textview_date_of_weekplan_row.setText(dateHeader);
  }
  
  /**
   * Shows how many rows the Adapter needs to render. The list adds two because it always needs to render the header
   * and the footer.
   *
   * @return ListSize + 2;
   */
  @Override
  public int getItemCount() {
    return userPlannedRecipeList.size() + 2;
  }
  
  /**
   * Returns the ViewType (Header, ItemRow or Footer) for a given position in the list.
   *
   * @param position Which item from the list it requires to get viewType clarification
   *
   * @return TYPE_HEADER = 0, TYPE_ITEM= 1, TYPE_FOOTER  = 2;
   */
  @Override
  public int getItemViewType(int position) {
    if (isPositionHeader(position)) {
      return TYPE_HEADER;
    } else if (isPositionFooter(position)) {
      return TYPE_FOOTER;
    }
    return TYPE_ITEM;
  }
  
  /**
   * Returns a Position for the getItemView Type
   *
   * @param position Which position the needs to be clarified
   *
   * @return True if position is 0 (Header), otherwise false.
   */
  private boolean isPositionHeader(int position) {
    return position == 0;
  }
  
  /**
   * Returns a Position for the getItemView Type
   *
   * @param position Which position the needs to be clarified
   *
   * @return True if position is listSize+1 (Footer), otherwise false.
   */
  private boolean isPositionFooter(int position) {
    return position == userPlannedRecipeList.size() + 1;
  }
  
  
  /*BEGIN VIEWHOLDERS*/
  
  /**
   * Sets the ViewHolder for HeaderItems. These are the Dates which group the planned recipes.
   */
  private class WeekplanHeaderViewHolder extends RecyclerView.ViewHolder {
    final TextView textview_date_of_weekplan_row;
    
    /**
     * Initializes the textView of the Header, which will be populated with a String date in the onBind method
     */
    WeekplanHeaderViewHolder(View view) {
      super(view);
      this.textview_date_of_weekplan_row = (TextView) itemView.findViewById(R.id.textview_date_of_weekplan_row);
    }
  }
  
  /**
   * Sets the ViewHolder for RowItems. These are planned Recipes.
   */
  //TODO Implement functionality which allows the user to click a planned recipe and call the recipes Detailed View.
  private class WeekplanItemViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView_thumbnail_recipe_image;
    final TextView textView_recipe_name;
    final TextView textView_short_recipe_description;
    final TextView textView_cookingTime;
    final ImageButton imageButton_remove_recipe_from_planning;
    final RelativeLayout relativelayout_recipe_container;
    
    /**
     * Initializes the thumbnail (imageView), texts and button and ImageView
     */
    WeekplanItemViewHolder(View view) {
      super(view);
      this.imageView_thumbnail_recipe_image = (ImageView) view.findViewById(R.id.imageView_thumbnail_recipe_image);
      this.textView_recipe_name = (TextView) view.findViewById(R.id.textView_recipe_name);
      this.textView_short_recipe_description = (TextView) view.findViewById(R.id.textView_short_recipe_description);
      this.textView_cookingTime = (TextView) view.findViewById(R.id.cookingTime);
      this.imageButton_remove_recipe_from_planning =
          (ImageButton) view.findViewById(R.id.imageButton_remove_recipe_from_planning);
      this.relativelayout_recipe_container = (RelativeLayout) view.findViewById(R.id.relativelayout_recipe_container);
      
    }
  }
  
  /**
   * Sets teh ViewHolder for the Footer, which allows the user to add recipes to a dayPlan.
   */
  private class WeekplanFooterViewHolder extends RecyclerView.ViewHolder {
    final Button button_plan_recipes_for_this_day;
    
    /**
     * Initializes the button to add recipes to a given day.
     */
    WeekplanFooterViewHolder(View view) {
      super(view);
      this.button_plan_recipes_for_this_day = (Button) view.findViewById(R.id.button_plan_recipes_for_this_day);
    }
  }
  /*END VIEWHOLDERS*/
  
}
