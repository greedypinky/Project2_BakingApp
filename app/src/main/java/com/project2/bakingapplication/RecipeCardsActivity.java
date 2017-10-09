package com.project2.bakingapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.test.espresso.IdlingResource;

import com.project2.bakingapplication.IdlingResource.ActivityIdlingResource;
import com.project2.bakingapplication.utilities.NetworkUtils;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.RecipeJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Need to read recipe from here !! not from file
 * https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
 */
public class RecipeCardsActivity extends AppCompatActivity implements RecipeCardAdapter.OnClickRecipeCardListener , LoaderManager.LoaderCallbacks {

    private static String TAG = RecipeCardsActivity.class.getSimpleName();
    private RecyclerView mCardRecycleView;
    private RecipeCardAdapter mAdapter;
    private TextView mNoRecipeTextView;
    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();
    private static int LOADER_ID = 1000;
    LoaderManager.LoaderCallbacks loaderCallbacks;
    private String mRecipeJSON = null;
    private ProgressBar mLoadingIndicator = null;
    private String RECIPE_KEY = "recipe_key";


    // add for testing
    @Nullable
    private ActivityIdlingResource mIdlingResource;


    // add for testing
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new ActivityIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the Card View Layout
        setContentView(R.layout.activity_recycler_view_for_recipe);

        mNoRecipeTextView = (TextView)findViewById(R.id.text_no_data_error);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mCardRecycleView = (RecyclerView)findViewById(R.id.recipe_recycler_view);
        mCardRecycleView.setHasFixedSize(true);
        mCardRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RecipeCardAdapter(this);
        mCardRecycleView.setAdapter(mAdapter);


        // check the savedInstanceState otherwise init the data from JSON file
        if(savedInstanceState!=null) {

            // get the saved states
            if (savedInstanceState.containsKey(RECIPE_KEY)) {
                mRecipes = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
                mAdapter.setAdapterData(mRecipes);
            }


        } else {
            // Get Recipe Data
            // use LoaderManager to get the data in a seperate thread
            loaderCallbacks = RecipeCardsActivity.this;
            Log.d(TAG,"initLoader");
            getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
        }

        mAdapter.setAdapterData(mRecipes);

        // Get the IdlingResource instance
        getIdlingResource();
    }


    /**
     * showErrorMessage
     */
    public void showErrorMessage() {

        mNoRecipeTextView.setVisibility(View.VISIBLE);
        mCardRecycleView.setVisibility(View.INVISIBLE);
    }

    // Implements the call back to handle when the card view is clicked
    @Override
    public void onClickRecipeCard(Recipe recipe) {
        Log.d(TAG, "onClickRecipeCard");
        Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
        intent.putExtra(RecipeStepsActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }



    // ***** Loader Manager callback methods ******
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "LoaderManager Callback:onCreateLoader");
        return new AsyncTaskLoader(getApplicationContext()) {
            @Override
            public List<Recipe> loadInBackground() {
                Log.d(TAG, "AsyncTaskLoader:loadInBackground");
                List<Recipe> recipes = new ArrayList<Recipe>();
                try {
                    URL recipeURL = NetworkUtils.getRecipeURL();
                    String response = NetworkUtils.getResponseFromHttp(recipeURL);
                    Log.d(TAG, "AsyncTaskLoader - HTTP Response:" + response);
                    recipes = RecipeJsonUtils.readRecipeArray(getApplicationContext(),response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return recipes;
            }

            @Override
            protected void onStartLoading() {
                Log.d(TAG, "AsyncTaskLoader:onStartLoading");
                super.onStartLoading();
                if (mRecipes != null && mRecipes.size() > 0) {
                    deliverResult(mRecipes);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    Log.d(TAG, "forceLoad");
                    forceLoad(); // force to load again - loadInBackground
                }
            }

            @Override
            public void deliverResult(Object data) {
                Log.d(TAG, "AsyncTaskLoader:deliverResult");
                super.deliverResult(data);
                if (data!=null) {
                    mRecipes = (ArrayList<Recipe>)(data) ;
                    Log.d(TAG, "DeliverResult list size:" + mRecipes.size());
                    if(mAdapter!=null) {
                        mAdapter.setAdapterData(mRecipes);
                    }
                }
            }
        };
    }

    // LoadManager Callback method
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d(TAG, "LoaderManager:onLoadFinished");
        if (loader instanceof AsyncTaskLoader) {
            Log.d(TAG, "AsyncTaskLoader:onLoadFinished");
            // finish loading - dismiss the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data == null) {
                // show error if no data
                showErrorMessage();
                //isErrorOccurs = true;
                Log.e(TAG, "Unable to get Recipe data! Please check the URI link");
            } else {
                // if data is returned, update Adapter's data
                Log.d(TAG,"Set recipe data to adapter" );
                mAdapter.setAdapterData(mRecipes);

            }
            // get back data from asynctask - set Idle state to true
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        }

    }

    // LoadManager Callback method
    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     * onSaveInstanceState
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_KEY, mRecipes);
    }
}
