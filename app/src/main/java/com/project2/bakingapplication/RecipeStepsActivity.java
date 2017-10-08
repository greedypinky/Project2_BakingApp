package com.project2.bakingapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity implements RecipeStepsFragment.OnFragmentClickListener {

    private static String TAG = RecipeStepsActivity.class.getSimpleName();
    private RecyclerView mIngredientRecycler;
    private RecyclerView mRecipeStepsDescRecycler;
    private Recipe mRecipe;
    public static final String RECIPE_KEY = "recipe";
    public static final String WIDGET_RECIPE_KEY = "widget_recipe";
    // add the boolean to detect if it is a tablet device
    boolean mTwoPane = false;
    private RecipeStepsFragment mRecipeStepsFragment;
    private TextView mTextNoVideo;
    private TextView mStepInstructions;
    private Uri mVideoURI;
    private SimpleExoPlayerView mStepVideoView;
    private Step mCurrentStep;
    private SimpleExoPlayer mExoPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(savedInstanceState!= null) {
            if(savedInstanceState.containsKey(RECIPE_KEY)) {

                mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            }

        } else {
            // get the recipe object passed by Intent's Extra
            Intent intent = getIntent();
            // recipe selected from the card view activity
            if (intent.hasExtra(RecipeStepsActivity.RECIPE_KEY)) {
                mRecipe = intent.getParcelableExtra(RecipeStepsActivity.RECIPE_KEY);
            }
            // recipe selected from the widget's grid view
            if (intent.hasExtra(RecipeStepsActivity.WIDGET_RECIPE_KEY)) {
                mRecipe = intent.getParcelableExtra(RecipeStepsActivity.WIDGET_RECIPE_KEY);
            }
        }

        // Determine if you're creating a two-pane or single-pane display
        // Two-pane view : with detail view on the right
        if (findViewById(R.id.tablet_recipe_detail) != null) {
            Log.d(TAG,"Tablet detail view is found!");
            // This recipe detail will only initially exist in the two-pane tablet case
            mTwoPane = true;
            // Initialize TextView - place holder when no video
            mTextNoVideo = (TextView) findViewById(R.id.text_no_video);
            // Initialize the exoplayer view.
            mStepVideoView = (SimpleExoPlayerView) findViewById(R.id.recipe_step_video);
            mStepInstructions = (TextView) findViewById(R.id.recipe_step_instructions);

            // Initialize the fragment
            mRecipeStepsFragment  = (RecipeStepsFragment)fragmentManager.findFragmentById(R.id.fragment_recipe_steps);

        } else {
            // one-pane view: without detail view on the right
            mRecipeStepsFragment = (RecipeStepsFragment) fragmentManager.findFragmentById(R.id.fragment_recipe_steps);
        }

        // Set Recipe data to fragment
        mRecipeStepsFragment.setRecipe(mRecipe);
        // set default step to first step
        if(mTwoPane) {
            Log.d(TAG,"Tablet detail view is found!");
            if(mRecipe.getStepList()!= null && (mRecipe.getStepList().size() > 0)) {
                setStepData(mRecipe.getStepList().get(0));
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

        if(!mTwoPane) {
            //Toast.makeText(this,"Fragment is Clicked",Toast.LENGTH_LONG).show();
            // TODO: start activity if in Portrait view
            Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
            intent.putExtra("STEPS", step);
            intent.putParcelableArrayListExtra("STEP_ARRAY", (ArrayList) mRecipe.getStepList());
            startActivity(intent);
        } else {
            // update the right pane
            setStepData(step);
        }
    }

    /**
     * setStepData
     * @param step
     */
    public void setStepData(Step step){
        mCurrentStep = step;
        // mCurrentStepIndex = step.getStepId();
        Log.d(TAG, "setStepData");
        mStepInstructions.setText(step.getDescription());
        String videoURL = step.getVideoURL();
        if(videoURL != null && !videoURL.isEmpty()) {

            //String dummyURI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdb3a_3-mix-sugar-salt-together-cheesecake/3-mix-sugar-salt-together-cheesecake.mp4";
            //mVideoURI =  Uri.parse(dummyURI);
            mVideoURI =  Uri.parse(videoURL);
            Log.d(TAG, "Video URI is:" + mVideoURI.toString());
            // TODO: check how to use Media play to play the movie
            mTextNoVideo.setVisibility(View.INVISIBLE);
            mStepVideoView.setVisibility(View.VISIBLE);

            // Initialize the player.
            // initializePlayer(mVideoURI);
            if(mExoPlayer == null) {
               // mExoPlayer = PlayVideoUtils.initializePlayer(mVideoURI,getApplicationContext(),RecipeStepsActivity.class);
               initializePlayer(mVideoURI);
            } else {
                //PlayVideoUtils.setMediaSourceOnly(mVideoURI,getApplicationContext(), RecipeStepsActivity.class, mExoPlayer);
                initializePlayer(mVideoURI);
            }

        } else {
            // No video data
            mTextNoVideo.setVisibility(View.VISIBLE);
            mStepVideoView.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer(Uri uri) {
        String userAgent = Util.getUserAgent(getApplicationContext(), RecipeStepsActivity.class.getName());
        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);
            mStepVideoView.setPlayer(mExoPlayer);
            // Prepare the MediaSource the very first time after ExoPlayer is initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getApplicationContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        } else {
            // Prepare the MediaSource after Exoplayer is already initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getApplicationContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayVideoUtils.releasePlayer(mExoPlayer);
    }
}
