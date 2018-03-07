package ch.zhaw.it15a_zh.psit3_03.mealmanager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.RecipeOverviewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.ShoppingListViewActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.activities.WeekPlanActivity;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.ann.ANNService;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBHelper;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.DBManager;

import java.util.Locale;

/**
 * MainActivity handles starting of the app.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide android top menu
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //create dbHelper for SQLite. Enables SQLite Work with ch.zhaw.it15a_zh.psit3_03.mealmanager.database
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "mmdb_presentation");
        DBManager.initializeInstance(dbHelper);
        dbHelper.createDataBase();

        //Button to start the shopping list activity, currently used to start the recipe detailed view activity
        Button button_shoppingList = (Button) findViewById(R.id.button_shoppingList);
        button_shoppingList.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingListViewActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //Button to start the recipe activity
        Button recipesButton = (Button) findViewById(R.id.button_recipes);
        recipesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipeOverviewActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //Button to start the mealProposal activity
        Button mealProposalButton = (Button) findViewById(R.id.button_meal_proposal);
        mealProposalButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeekPlanActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //Start the ann service
        //Intent annIntent = new Intent(getApplicationContext(), ANNService.class);
        //getApplicationContext().startService(annIntent);
    }
}