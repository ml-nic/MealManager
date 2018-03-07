package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeOverviewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.utility.SortState;

/**
 * Adapter for the different sortoptions.
 */
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.MyViewHolder> {
  private final Uri uriAscending;
  private final Uri uriDescending;
  private final Uri uriEmptyimage;
  private LinkedHashMap<String, SortState> currentSortList;
  private Context mContext;
  private RecipeOverviewAdapter recipeOverviewAdapter;
  private ArrayList<MyViewHolder> holderList = null;
  
  public SortAdapter(Context mContext, RecipeOverviewAdapter recipeOverviewAdapter) {
    currentSortList = new LinkedHashMap<>();
    this.mContext = mContext;
    this.recipeOverviewAdapter = recipeOverviewAdapter;
    
    currentSortList.put("Name", SortState.ASCENDING);
    currentSortList.put("Zubereitungszeit", SortState.NONE);
    currentSortList.put("HealthRating", SortState.NONE);
    uriAscending = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/ascending");
    uriDescending = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/descending");
    uriEmptyimage = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/emptyimage");
    
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
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_list_row, parent, false);
    return new MyViewHolder(itemView);
  }

  /**
   * Handles binding of View
   * @param holder The ViewHolder
   * @param position The position of the current element.
   */
  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {
    holderList.add(holder);
    
    final String sortName = getSortNameByPosition(position);
    final Uri uri = getCorrespondingUri(currentSortList.get(sortName));
    
    Picasso.with(mContext).load(uri).error(R.drawable.emptyimage).placeholder(R.drawable.emptyimage)
        .into(holder.imageState);
    holder.sortName.setText(sortName);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SortState newState = currentSortList.get(sortName).next();
        currentSortList.put(sortName, newState);
        
        Uri uri = getCorrespondingUri(newState);
        Picasso.with(mContext).load(uri).error(R.drawable.emptyimage).placeholder(R.drawable.emptyimage)
            .into(holder.imageState);
        sortRecipes(sortName, newState, holder);
        RecipeOverviewActivity.closeDrawer();
      }
    });
  }
  
  private void sortRecipes(String sortName, SortState newState, MyViewHolder holder) {
    if (sortName.equalsIgnoreCase("name")) {
      recipeOverviewAdapter.sortRecipesByName(newState);
      clearOtherImages(holder.getAdapterPosition());
    } else if (sortName.equalsIgnoreCase("zubereitungszeit")) {
      recipeOverviewAdapter.sortRecipesByCookingtime(newState);
      clearOtherImages(holder.getAdapterPosition());
    } else if (sortName.equalsIgnoreCase("healthrating")) {
      recipeOverviewAdapter.sortRecipesByHealthrating(newState);
      clearOtherImages(holder.getAdapterPosition());
    }
  }
  
  private void clearOtherImages(int position) {
    for (MyViewHolder currentHolder : holderList) {
      if (currentHolder.getAdapterPosition() != position) {
        Picasso.with(mContext).load(uriEmptyimage).error(R.drawable.emptyimage).placeholder(R.drawable.emptyimage)
            .into(currentHolder.imageState);
      }
    }
  }
  
  private Uri getCorrespondingUri(SortState sortState) {
    Uri uri = uriEmptyimage;
    if (sortState == SortState.ASCENDING) {
      uri = uriAscending;
    } else if (sortState == SortState.DESCENDING) {
      uri = uriDescending;
    }
    return uri;
  }
  
  private String getSortNameByPosition(int position) {
    Set<String> t = currentSortList.keySet();
    int counter = 0;
    String sortName = "";
    for (String a : t) {
      if (counter == position) {
        sortName = a;
      }
      counter++;
    }
    return sortName;
  }
  
  @Override
  public int getItemCount() {
    return currentSortList.size();
  }
  
  /**
   * Nested class to hold the recyclerview
   */
  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView sortName;
    public ImageView imageState;
    
    public MyViewHolder(View view) {
      super(view);
      sortName = (TextView) view.findViewById(R.id.textView_sortName);
      imageState = (ImageView) view.findViewById(R.id.image_state_icon);
    }
  }
}