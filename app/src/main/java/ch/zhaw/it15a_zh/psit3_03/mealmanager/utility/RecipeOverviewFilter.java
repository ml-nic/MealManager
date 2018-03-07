package ch.zhaw.it15a_zh.psit3_03.mealmanager.utility;

import android.widget.Filter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.adapters.RecipeOverviewAdapter;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter class for recipeOverview, handles filtering.
 */
public class RecipeOverviewFilter extends Filter {
    private RecipeOverviewAdapter mAdapter;
    private List<Recipe> currentList;
    private List<Recipe> filteredList;

    public RecipeOverviewFilter(RecipeOverviewAdapter recipeOverviewAdapter, List<Recipe> currentList) {
        super();
        this.mAdapter = recipeOverviewAdapter;
        this.currentList = currentList;
        this.filteredList = new ArrayList<>();
    }

    /**
     * Performs filtering by constraint.
     *
     * @param constraint The constraint for filtering. Example: <filterPattern>;<filterType>
     * @return The filterResults
     */
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        FilterResults results = new FilterResults();
        if (constraint.length() == 0) {
            filteredList.addAll(currentList);
        } else {
            String[] constraints = constraint.toString().trim().split(";");
            String filterPattern = constraints[0];
            String filterType = constraints[1];

            switch (filterType) {
                case "type":
                    for (Recipe recipe : currentList) {
                        if (recipe.isTypeOf(filterPattern)) {
                            filteredList.add(recipe);
                        }
                    }
                    break;
                case "kind":
                    for (Recipe recipe : currentList) {
                        if (recipe.isCourseOf(filterPattern)) {
                            filteredList.add(recipe);
                        }
                    }
                    break;
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    /**
     * Publish the results.
     *
     * @param constraint    The constraint.
     * @param filterResults The filterResults
     */
    @Override
    protected void publishResults(CharSequence constraint, FilterResults filterResults) {
        mAdapter.filteredList.clear();
        mAdapter.filteredList.addAll((ArrayList<Recipe>) filterResults.values);
        mAdapter.useFilteredList();
    }
}