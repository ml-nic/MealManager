package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.RecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.RecipeItem;

public class RecipeDetailedViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int HEADER = 0;
  private final List<RecipeItem> recipeItemList;
  private final Context context;
  private final int recipeID;
  
  public RecipeDetailedViewAdapter(Context context, List<RecipeItem> recipeItems, int recipeID) {
    this.recipeItemList = recipeItems;
    this.context = context;
    this.recipeID = recipeID;
  }
  
  /**
   * Create the ViewHolder for the respective views and inflates them
   *
   * @param parent Can be the HeaderView or ListView
   * @param viewType Int used to determine which View it is supposed to inflate
   *
   * @return returns a viewholder object specific to either header or list item
   */
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == HEADER) {
      View headerView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_detailed_header, parent, false);
      return new HeaderViewHolder(headerView);
    } else {
      View itemView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detailed_item_row, parent, false);
      return new ListViewHolder(itemView);
    }
  }
  
  /**
   * Assigned the text to re repective textViews and later Image
   *
   * @param holder Holds the respective view. Either the Header or an list item
   * @param position Where in the recyclerLayout the list currently is. Used to determine which textViews need to be
   * set
   */
  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    RecipeRepo rr = new RecipeRepo();
    Recipe recipe = rr.findOneByID(recipeID);
    ItemRepo ir = new ItemRepo();
    if (holder instanceof HeaderViewHolder) {
      HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
      headerViewHolder.textView_recipe_title.setText(recipe.getName());
      headerViewHolder.textView_preparation_time.setText((String.valueOf(recipe.getCookingTime())));
      headerViewHolder.ratingBar_health_rating_bar.setRating((float) recipe.getHealthRating());
      headerViewHolder.textView_preparation_instruction.setText(recipe.getPreparation());
      headerViewHolder.textView_servings.setText(String.valueOf(recipe.getServings()));
      String imageID = recipe.getImage();
      imageID = imageID.substring(0, imageID.lastIndexOf("."));
      Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/" + imageID);
      Picasso.with(context).load(uri).error(R.drawable.emptyimage).placeholder(R.drawable.emptyimage)
          .into(((HeaderViewHolder) holder).imageView_recipeImage);
      
    } else {
      RecipeItem recipeItem = recipeItemList.get(position - 1);
      ListViewHolder listViewHolder = (ListViewHolder) holder;
      listViewHolder.textView_itemName.setText(ir.findOneByID(recipeItem.getItemID()).getName());
      if(String.valueOf(recipeItem.getAmount()).endsWith(".0")){
        listViewHolder.textView_itemAmount.setText(String.valueOf((int)recipeItem.getAmount()));
      }else {
        listViewHolder.textView_itemAmount.setText(String.valueOf(recipeItem.getAmount()));
      }
      listViewHolder.textView_itemUnit.setText(recipeItem.getUnit());
    }
  }
  
  /**
   * Gets the size of the list. +1 because this recyclerview has a header
   *
   * @return int list size
   */
  @Override
  public int getItemCount() {
    return recipeItemList.size() + 1;
  }
  
  /**
   * Returns the itemViewType needed for the onBindViewHolder
   *
   * @param position Current position of the recyclerview
   *
   * @return int of current position
   */
  @Override
  public int getItemViewType(int position) {
    if (position == HEADER) {
      return HEADER;
    }
    return 1;
  }
  
  /**
   * Creates the Header for the recipe view. The Header includes the recipe name, an image, the cookingtime and the
   * healthrating
   */
  public class HeaderViewHolder extends RecyclerView.ViewHolder {
    final TextView textView_recipe_title;
    final ImageView imageView_recipeImage;
    final TextView textView_preparation_time_colon;
    final TextView textView_preparation_time;
    final TextView textView_time_unit_minutes;
    final RatingBar ratingBar_health_rating_bar;
    final TextView healthRating;
    final TextView textView_ingredients_colon;
    final TextView textView_preparation_instruction;
    final TextView textView_servings_colon;
    final TextView textView_servings;
  
    
  
    public HeaderViewHolder(View view) {
      super(view);
      this.textView_recipe_title = (TextView) view.findViewById(R.id.textView_recipe_name);
      this.imageView_recipeImage = (ImageView) view.findViewById(R.id.imageView_recipeImage);
      this.textView_preparation_time_colon = (TextView) view.findViewById(R.id.textView_preparation_time_colon);
      this.textView_preparation_time = (TextView) view.findViewById(R.id.textView_preparation_time);
      this.textView_time_unit_minutes = (TextView) view.findViewById(R.id.textView_time_unit_minutes);
      this.ratingBar_health_rating_bar = (RatingBar) view.findViewById(R.id.ratingBar_health_rating_bar);
      this.healthRating = (TextView) view.findViewById(R.id.healthRating);
      this.textView_ingredients_colon = (TextView) view.findViewById(R.id.textView_ingredients_for);
      this.textView_preparation_instruction = (TextView) view.findViewById(R.id.textView_preparation_instructions);
      this.textView_servings_colon = (TextView) view.findViewById(R.id.textView_servings_colon);
      this.textView_servings = (TextView) view.findViewById(R.id.textView_servings);
    }
  }
  
  /**
   * Creates the list for the recipe items. The list includes the item name (ie. milk, bred, butter etc.) the unit (ie
   * . liters, grams etc.) and the amount required
   */
  public class ListViewHolder extends RecyclerView.ViewHolder {
    final TextView textView_itemName;
    final TextView textView_itemAmount;
    final TextView textView_itemUnit;
    
    public ListViewHolder(View view) {
      super(view);
      this.textView_itemName = (TextView) view.findViewById(R.id.textView_itemName);
      this.textView_itemUnit = (TextView) view.findViewById(R.id.textView_itemUnit);
      this.textView_itemAmount = (TextView) view.findViewById(R.id.textView_itemAmount);
    }
  }
}
