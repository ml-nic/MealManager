package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeOverviewActivity;

/**
 * Adapter for the different filteroptions.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
  private final List<String> artNameList;
  private final String filterIdentifier;
  private RecipeOverviewAdapter recipeOverviewAdapter;
  private ArrayList<FilterAdapter.MyViewHolder> holderList = null;
  
  public FilterAdapter(List<String> artNameList, RecipeOverviewAdapter recipeOverviewAdapter, String filterIdentifier) {
    this.artNameList = artNameList;
    this.recipeOverviewAdapter = recipeOverviewAdapter;
    this.filterIdentifier = filterIdentifier;
    holderList = new ArrayList<>();
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
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_row, parent, false);
    return new MyViewHolder(itemView);
  }
  
  /**
   * Gets the filteroptions and display each option in the recyclerview.
   * And adds an onClickListener for each item
   *
   * @param holder The ViewHolder
   * @param position The position of the item
   */
  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {
    holderList.add(holder);
    
    final String art = artNameList.get(position);
    holder.artName.setText(art);
    
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        recipeOverviewAdapter.resetFilter();
        recipeOverviewAdapter.getFilter().filter(art + ";" + filterIdentifier);
        holder.imageView_checkmark.setVisibility(View.VISIBLE);
        clearOtherImages(holder.getAdapterPosition());
        RecipeOverviewActivity.hideFilterList();
        RecipeOverviewActivity.closeDrawer();
      }
    });
  }
  
  /**
   * Set images of every filter to View.GONE, except the matching image for the current filteroption.
   *
   * @param position The position of the current filteroption.
   */
  public void clearOtherImages(int position) {
    for (FilterAdapter.MyViewHolder currentHolder : holderList) {
      if (currentHolder.getAdapterPosition() != position) {
        currentHolder.imageView_checkmark.setVisibility(View.GONE);
      }
    }
  }
  
  @Override
  public int getItemCount() {
    return artNameList.size();
  }
  
  /**
   * Nested class to hold the recyclerview
   */
  public class MyViewHolder extends RecyclerView.ViewHolder {
    public final TextView artName;
    public ImageView imageView_checkmark;
    
    public MyViewHolder(View view) {
      super(view);
      artName = (TextView) view.findViewById(R.id.textView_artName);
      imageView_checkmark = (ImageView) view.findViewById(R.id.image_checkmark);
    }
  }
}