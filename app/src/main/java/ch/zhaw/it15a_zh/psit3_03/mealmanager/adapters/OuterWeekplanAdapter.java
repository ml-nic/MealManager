package ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.R;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.PlannedRecipeRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.UserPlannedRecipesRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.UserPlannedRecipe;

/**
 * The OuterWeekPlanAdapter handles the CardLayouts.
 */
public class OuterWeekplanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<String> distinctListOfPlannedDates;
  private final UserPlannedRecipesRepo userPlannedRecipesRepo = new UserPlannedRecipesRepo();
  private Context context;
  
  /**
   * Adapter which is responsible for populating individual rows with CardViews containing instances of
   * innerRecyclerViewAdapters
   *
   * @param applicationContext Originating activity context
   * @param distinctListOfPlannedDates List of Dates for which the user has planned recipes including new dates he may
   * have selected in the DatePicker activity
   */
  public OuterWeekplanAdapter(Context applicationContext, List<String> distinctListOfPlannedDates) {
    context = applicationContext;
    this.distinctListOfPlannedDates = distinctListOfPlannedDates;
    
  }
  
  /**
   * Makes the Layout of the CardView available to the onBind method
   *
   * @param parent N/A
   * @param viewType N/A :: Becase there is only one viewType
   *
   * @return new Instance of a OuterWeekPlanViewHolder
   */
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weekplan_cardview, parent, false);
    return new OuterWeekplanViewHolder(v);
  }
  
  /**
   * Populates the individual rows with innerRecyclerView Adapters
   *
   * @param holder In this case always the OuterWeekPlanViewHolder
   * @param position Which row is currently being populated with information
   */
  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    //Creates OuterViewHolder and Assigns InnerViewHolder per "row"
    OuterWeekplanViewHolder outerWeekplanViewHolder = (OuterWeekplanViewHolder) holder;
    List<UserPlannedRecipe> userPlannedRecipeList =
        userPlannedRecipesRepo.getUserPlannedRecipeFromSpecificDate(distinctListOfPlannedDates.get(position));
    final String dateHeader = distinctListOfPlannedDates.get(position);
    InnerWeekplanAdapter innerWeekplanAdapter = new InnerWeekplanAdapter(dateHeader, userPlannedRecipeList, context);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
    setRecyclerviewOptions(holder, outerWeekplanViewHolder, dateHeader, innerWeekplanAdapter, manager);
  }
  
  private void setRecyclerviewOptions(final RecyclerView.ViewHolder holder,
      OuterWeekplanViewHolder outerWeekplanViewHolder, final String dateHeader,
      InnerWeekplanAdapter innerWeekplanAdapter, RecyclerView.LayoutManager manager) {
    outerWeekplanViewHolder.recyclerView.setLayoutManager(manager);
    outerWeekplanViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
    outerWeekplanViewHolder.recyclerView.setAdapter(innerWeekplanAdapter);
    
    outerWeekplanViewHolder.button_delete_day_from_planning
        .setOnClickListener(setOnClickListenerToRemoveEntireDayFromPlan(holder, dateHeader));
  }
  
  private View.OnClickListener setOnClickListenerToRemoveEntireDayFromPlan(final RecyclerView.ViewHolder holder,
      final String dateHeader) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PlannedRecipeRepo plannedRecipeRepo = new PlannedRecipeRepo();
        plannedRecipeRepo.removeDate(dateHeader);
        List<UserPlannedRecipe> userPlannedRecipeList =
            userPlannedRecipesRepo.getUserPlannedRecipeFromSpecificDate(dateHeader);
        for (UserPlannedRecipe userPlannedRecipe : userPlannedRecipeList) {
          userPlannedRecipesRepo
              .removePlannedRecipe(userPlannedRecipe.getRecipeID(), userPlannedRecipe.getDatePlanned());
        }
        distinctListOfPlannedDates.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
        notifyItemRangeChanged(holder.getAdapterPosition(), distinctListOfPlannedDates.size());
      }
    };
  }
  
  /**
   * Responsible for determining the amount of rows which will be rendered in total
   *
   * @return size of distinctListOfPlannedDates
   */
  @Override
  public int getItemCount() {
    return distinctListOfPlannedDates.size();
  }
  
  /**
   * Sets the ViewHolders for the OuterViewHolder. In this case one itemRow contains an instance of
   * innerRecyclerView, which in turn contains a Header (date), planned Recipes (if any have been planned, otherwise
   * empty) and a footer.
   */
  public class OuterWeekplanViewHolder extends RecyclerView.ViewHolder {
    public final RecyclerView recyclerView;
    public final Button button_delete_day_from_planning;
    
    /**
     * Initializes the innerRecyclerView and the 'delete-entire-day-from-planning Buttun making it available to the
     * onBind method.
     *
     * @param itemView N/A becase there is only one type of item
     */
    public OuterWeekplanViewHolder(View itemView) {
      super(itemView);
      this.recyclerView = (RecyclerView) itemView.findViewById(R.id.activity_weekplan_inner_recyclerview);
      this.button_delete_day_from_planning = (Button) itemView.findViewById(R.id.button_delete_day_from_planning);
    }
  }
  
}
