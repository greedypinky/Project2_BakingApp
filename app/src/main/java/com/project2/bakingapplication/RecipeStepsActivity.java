package com.project2.bakingapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.project2.bakingapplication.utilities.Ingredient;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.Step;

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity implements RecipeStepsFragment.OnFragmentClickListener {

    private static String TAG = RecipeStepsActivity.class.getSimpleName();
    private RecyclerView mIngredientRecycler;
    private RecyclerView mRecipeStepsDescRecycler;
    private Recipe mRecipe;
    private static final String  RECIPE_KEY = "recipe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        if(savedInstanceState!= null) {
            if(savedInstanceState.containsKey(RECIPE_KEY)) {

                mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            }

        } else {
            // fragment will be injected to this Activity
            Intent intent = getIntent();
            //TODO : Fix this later, lets use some dummy data
            // selected Recipe
            mRecipe = intent.getParcelableExtra("RECIPE");
        }
        // from intent get back the recipe ID
        // TODO: say we can get the Recipe ID and then how do we pass it into the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepsFragment recipeStepsFragment  = (RecipeStepsFragment)fragmentManager.findFragmentById(R.id.fragment_recipe_steps);

        recipeStepsFragment.setRecipe(mRecipe);
        //TODO: when do try the dummy data
        //recipeStepsFragment.setRecipe(getDummyRecipe());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Dummy Test Data
    private Recipe getDummyRecipe() {

        ArrayList<Step> steps = new ArrayList<Step>();
        ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();

        Step step = new Step("1",
                "Recipe Introduction",
                "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                "thumbnailURL");

        Ingredient ingred = new Ingredient("1",
                "cup",
                "sugar");

        steps.add(step);
        ingreds.add(ingred);
        return new Recipe("1","cupcakes", ingreds,steps,"8","");
    }

    /**
     * onOptionsItemSelected - will be called when a toolbar menu is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // add this back to end the current activity
            case android.R.id.home:
                Log.d(TAG, "home/up button is clicked");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickFragment(Step step, int pos) {
        Toast.makeText(this,"Fragment is Clicked",Toast.LENGTH_LONG).show();
        // TODO: start activity if in Portrait view
        Intent intent = new Intent(getApplicationContext(),RecipeDetailActivity.class);
        intent.putExtra("STEPS",step);
        intent.putParcelableArrayListExtra("STEP_ARRAY", (ArrayList) mRecipe.getStepList());
        startActivity(intent);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
