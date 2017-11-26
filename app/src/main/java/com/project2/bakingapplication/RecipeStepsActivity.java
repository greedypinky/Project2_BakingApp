package com.project2.bakingapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.project2.bakingapplication.utilities.Ingredient;
import com.project2.bakingapplication.utilities.PlayVideoUtils;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity implements RecipeStepsFragment.OnFragmentClickListener {

    private static String TAG = RecipeStepsActivity.class.getSimpleName();
    private static String VIDEO_POSITION_KEY = "video_position";
    private static String CURRENT_STEP_KEY = "current_step";
    private static String CURRENT_WINDOW_POSITION_KEY = "current_window_position";
    private RecyclerView mIngredientRecycler;
    private RecyclerView mRecipeStepsDescRecycler;
    private Recipe mRecipe;
    public static final String RECIPE_KEY = "recipe";
    public static final String WIDGET_RECIPE_KEY = "widget_recipe";
    // add the boolean to detect if it is a tablet device
    boolean mTwoPane = false;
    private RecipeStepsFragment mRecipeStepsFragment;
    private StepDetailFragment mStepDetailFragment;
    private long mVideoPosition = -1;
    private int mCurrentwindowIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onDestroy");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        // mRecipeImageView = (ImageView) findViewById(R.id.recipe_image);
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Restore the SavedInstanceState
        if(savedInstanceState!= null) {
            if(savedInstanceState.containsKey(RECIPE_KEY)) {
                mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            }

            if(savedInstanceState.containsKey(VIDEO_POSITION_KEY)) {
                mVideoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
            }

            if(savedInstanceState.containsKey(CURRENT_WINDOW_POSITION_KEY)) {
                mCurrentwindowIndex = savedInstanceState.getInt(CURRENT_WINDOW_POSITION_KEY);
            }
        } else {
            // get the recipe object passed by Intent's Extra
            Intent intent = getIntent();
            // check if intent is null to avoid exception
            if (intent != null && intent.getExtras() != null) {
                // recipe selected from the card view activity
                if (intent.hasExtra(RecipeStepsActivity.RECIPE_KEY)) {
                    mRecipe = intent.getParcelableExtra(RecipeStepsActivity.RECIPE_KEY);
                }
                // recipe selected from the widget's grid view
//                if (intent.hasExtra(RecipeStepsActivity.WIDGET_RECIPE_KEY)) {
//                    mRecipe = intent.getParcelableExtra(RecipeStepsActivity.WIDGET_RECIPE_KEY);
//                }
            }
        }


        // TODO: load 2 fragments:- one for master and one for detail
        if (findViewById(R.id.fragment_recipe_steps) != null) {
            // one-pane view: without detail view on the right
            Log.d(TAG,"It is a one-pane view");
            mRecipeStepsFragment = (RecipeStepsFragment) fragmentManager.findFragmentById(R.id.fragment_recipe_steps);
        }
        else {
            mTwoPane = true;
            Log.d(TAG,"It is a two-pane view");
            // two-pane view: with detail view on the right
            // Initialize the Master fragment
            // get the fragment id in activity_receipt_steps2.xml
            mRecipeStepsFragment  = (RecipeStepsFragment)fragmentManager.findFragmentById(R.id.fragment_recipe_steps_land);
            // Hide the AppBar to show in full window mode
            // ActionBar bar = getSupportActionBar();
            // bar.hide(); // hide the AppBar

            // Initialize the Detail fragment
            mStepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentById(R.id.fragment_recipe_detail_land);
        }

        // Set Recipe data to fragment
        mRecipeStepsFragment.setRecipe(mRecipe);
        // set default step to first step
        if (mTwoPane) {
            Log.d(TAG,"Tablet detail view is found!");
            if (mRecipe.getStepList()!= null && (mRecipe.getStepList().size() > 0)) {
                //setStepData(mRecipe.getStepList().get(0));
                mStepDetailFragment.setStepData(mRecipe.getStepList().get(0));
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Dummy Test Data
//    private Recipe getDummyRecipe() {
//
//        ArrayList<Step> steps = new ArrayList<Step>();
//        ArrayList<Ingredient> ingreds = new ArrayList<Ingredient>();
//
//        Step step = new Step("1",
//                "Recipe Introduction",
//                "Recipe Introduction",
//                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
//                "thumbnailURL");
//
//        Ingredient ingred = new Ingredient("1",
//                "cup",
//                "sugar");
//
//        steps.add(step);
//        ingreds.add(ingred);
//        return new Recipe("1","cupcakes", ingreds,steps,"8","");
//    }

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

    // Fragment callback to pass in the Step information
    @Override
    public void onClickFragment(Step step, int pos) {

        // portrait mode
        if (!mTwoPane) {

            // start activity if in Portrait view
            Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
            intent.putExtra("STEPS", step);
            intent.putParcelableArrayListExtra("STEP_ARRAY", (ArrayList) mRecipe.getStepList());
            startActivity(intent);

            // landscape mode
        } else {
            // update the right pane 's step information
            mStepDetailFragment.setStepData(step);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
        // when in the landscape mode
        if(mTwoPane) {
            outState.putLong(VIDEO_POSITION_KEY, mVideoPosition);
            outState.putInt(CURRENT_WINDOW_POSITION_KEY, mCurrentwindowIndex);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume - initialize the player");
    }
}
