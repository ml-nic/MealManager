package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeDetailedViewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.RecipeOverviewFilter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.SortState;

/**
 * Adapter for the Recyclerview in recipe overview.
 */
public class RecipeOverviewAdapter extends RecyclerView.Adapter<RecipeOverviewAdapter.MyViewHolder>
    implements Filterable {
  public List<Recipe> filteredList;
  private List<Recipe> originalRecipesList;
  private List<Recipe> currentRecipeList;
  private RecipeOverviewFilter recipeOverviewFilter;
  private Context mContext;
  private String dateHeader;
  
  public RecipeOverviewAdapter(Context mContext, List<Recipe> recipesList, String dateHeader) {
    this.mContext = mContext;
    currentRecipeList = new ArrayList<>();
    originalRecipesList = new ArrayList<>();
    originalRecipesList.addAll(recipesList);
    currentRecipeList.addAll(recipesList);
    filteredList = new ArrayList<>();
    this.dateHeader = dateHeader;
  }
  
  /**
   * Returns the recipeOverviewFilter, if not exitst a new one is created.
   *
   * @return recipeOverviewFilter The corresponding recipeOverviewFilter.
   */
  @Override
  public Filter getFilter() {
    if (recipeOverviewFilter == null) {
      recipeOverviewFilter = new RecipeOverviewFilter(this, currentRecipeList);
    }
    return recipeOverviewFilter;
  }
  
  /**
   * Use filtered list instead of unfiltered.
   */
  public void useFilteredList() {
    currentRecipeList.clear();
    currentRecipeList.addAll(filteredList);
    this.notifyDataSetChanged();
  }
  
  /**
   * Creates the ViewHolder and loads the item layout
   *
   * @param parent The parent ViewGroup
   * @param viewType The viewType
   *
   * @return new ViewHolder which contains the item
   */
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_row, parent, false);
    return new MyViewHolder(itemView);
  }
  
  /**
   * Gets the recipes from ch.zhaw.it15a_zh.psit3_03.mealmanager.database and display each item in the recyclerview.
   * And add an onClickListener for the item
   *
   * @param holder The ViewHolder
   * @param position The position of the item
   */
  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    final Recipe recipe = currentRecipeList.get(position);
    
    String imageID = recipe.getImage();
    imageID = imageID.substring(0, imageID.lastIndexOf("."));
    Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/" + imageID);
    
    Picasso.with(mContext).load(uri).error(R.drawable.emptyimage).placeholder(R.drawable.emptyimage)
        .into(holder.imageView);
    holder.name.setText(recipe.getName());
    holder.description.setText(recipe.getDescription());
    holder.cookingTime.setText(mContext.getString(R.string.time) + recipe.getCookingTime());
    
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent recipeActivity = new Intent(mContext, RecipeDetailedViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dateHeader", dateHeader);
        bundle.putInt("recipeid", recipe.getRecipeID());
        recipeActivity.putExtras(bundle);
        
        mContext.startActivity(recipeActivity);
      }
    });
  }
  
  /**
   * Sorts the Recipelist by name with the nextState (SortState).
   *
   * @param nextState The next sortstate
   */
  public void sortRecipesByName(SortState nextState) {
    if (nextState == SortState.NONE) {
      currentRecipeList.clear();
      currentRecipeList.addAll(originalRecipesList);
    } else if (nextState == SortState.ASCENDING) {
      Collections.sort(currentRecipeList, Recipe.sortByNameAscending);
    } else {
      Collections.sort(currentRecipeList, Recipe.sortByNameDescending);
    }
    notifyDataSetChanged();
  }
  
  /**
   * Sorts the recipelist by healthrating with the nextState (SortState).
   *
   * @param nextState The next sortstate
   */
  public void sortRecipesByHealthrating(SortState nextState) {
    if (nextState == SortState.NONE) {
      currentRecipeList.clear();
      currentRecipeList.addAll(originalRecipesList);
    } else if (nextState == SortState.ASCENDING) {
      Collections.sort(currentRecipeList, Recipe.sortByHealthratingDescending);
    } else {
      Collections.sort(currentRecipeList, Recipe.sortByHealthratingAscending);
    }
    notifyDataSetChanged();
  }
  
  /**
   * Sorts the recipeslist by cookingtime with the nextState (SortState).
   *
   * @param nextState The next sortstate
   */
  public void sortRecipesByCookingtime(SortState nextState) {
    if (nextState == SortState.NONE) {
      currentRecipeList.clear();
      currentRecipeList.addAll(originalRecipesList);
    } else if (nextState == SortState.ASCENDING) {
      Collections.sort(currentRecipeList, Recipe.sortByCookingtimeAscending);
    } else {
      Collections.sort(currentRecipeList, Recipe.sortByCookingtimeDescending);
    }
    notifyDataSetChanged();
  }
  
  /**
   * Resets the filteroption and use the unfiltered list.
   */
  public void resetFilter() {
    currentRecipeList.clear();
    currentRecipeList.addAll(originalRecipesList);
    notifyDataSetChanged();
  }
  
  /**
   * Resets the sorting and use the unsorted list.
   */
  public void resetSort() {
    currentRecipeList.clear();
    currentRecipeList.addAll(originalRecipesList);
    notifyDataSetChanged();
  }
  
  @Override
  public int getItemCount() {
    return currentRecipeList.size();
  }

  public List<Recipe> getCurrentRecipeList() {
    return currentRecipeList;
  }

    /**
   * Nested class to hold the recyclerview
   */
  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView name, description, cookingTime;
    public ImageView imageView;
    
    public MyViewHolder(View view) {
      super(view);
      name = (TextView) view.findViewById(R.id.textView_recipe_name);
      description = (TextView) view.findViewById(R.id.textView_short_recipe_description);
      imageView = (ImageView) view.findViewById(R.id.imageView_thumbnail_recipe_image);
      cookingTime = (TextView) view.findViewById(R.id.cookingTime);
    }
  }
}