package com.project2.bakingapplication;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.project2.bakingapplication.utilities.BakingAppWidget;
import com.project2.bakingapplication.utilities.BakingAppWidget.UpdateWidgetService;
import com.project2.bakingapplication.utilities.Ingredient;
import com.project2.bakingapplication.utilities.NetworkUtils;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.RecipeJsonUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WidgetConfigurationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, OnItemSelectedListener {

    private static final String TAG = WidgetConfigurationActivity.class.getSimpleName() ;
    private int mAppWidgetId;
    private Spinner optionSpinner;
    private int selectedRecipeIndex = 0;
    private Button addButton;
    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();
    private static int LOADER_ID = 2000;
    LoaderManager.LoaderCallbacks loaderCallbacks;
    AppWidgetManager appWidgetManager;
    private static final String PREFS_NAME
            = "com.project2.bakingapplication.utilities.BakingAppWidget";
    private static final String PREF_PREFIX_KEY = "prefix_";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        // Perform your App Widget configuration.
        optionSpinner = (Spinner) findViewById(R.id.spinner_recipe);
        addButton = (Button) findViewById(R.id.btn_add);

        setResult(RESULT_CANCELED);

        // Need to get the application widget id from the Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // If they gave us an intent without the widget id, end the activity.
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(TAG,"Invalid App widget ID!");
                finish();
            }
        }

        // use LoaderManager to get the data in a seperate thread
        loaderCallbacks = WidgetConfigurationActivity.this;
        Log.d(TAG,"getSupportLoaderManager - get ricipe data!");
        getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppWidgetWithDesiredRecipe(selectedRecipeIndex);
                //addAppWidgetWithDesiredRecipe(pos);
                Log.d(TAG,"OnCreate - Update Widget based on Activity configuration");
            }
        });

        optionSpinner.setOnItemSelectedListener(this);
    }
    /*
    Caused by: android.os.BadParcelableException: ClassNotFoundException when unmarshalling: com.project2.bakingapplication.utilities.Recipe
    at android.os.Parcel.readParcelableCreator(Parcel.java:2535)
    at android.os.Parcel.readParcelable(Parcel.java:2461)
    at android.os.Parcel.readValue(Parcel.java:2364)
    at android.os.Parcel.readArrayMapInternal(Parcel.java:2717)
    at android.os.BaseBundle.unparcel(BaseBundle.java:269)
    at android.os.BaseBundle.getInt(BaseBundle.java:867)
    at android.content.Intent.getIntExtra(Intent.java:6146)
    at com.android.launcher3.Launcher.handleActivityResult(Launcher.java:743)
    at com.android.launcher3.Launcher.onActivityResult(Launcher.java:841)
    at android.app.Activity.dispatchActivityResult(Activity.java:6915)
    at android.app.ActivityThread.deliverResults(ActivityThread.java:4049)
    at android.app.ActivityThread.handleSendResult(ActivityThread.java:4096) 
    at android.app.ActivityThread.-wrap20(ActivityThread.java) 
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1516) 
    at android.os.Handler.dispatchMessage(Handler.java:102) 
    at android.os.Looper.loop(Looper.java:154) 
    at android.app.ActivityThread.main(ActivityThread.java:6077) 
    at java.lang.reflect.Method.invoke(Native Method) 
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:865) 
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:755) 
            10-21 12:27:09.328 1601-1790/system_process W/ActivityManager:   Force finishing activity com.android.launcher3/.Launcher

            */
    /**
     *
     */
    private void addAppWidgetWithDesiredRecipe(int position) {
        Log.d(TAG, "addAppWidgetWithDesiredRecipe for recipe in position:" + position);
        // When the configuration is complete, get an instance of the AppWidgetManager
        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        Recipe selectedRecipe = mRecipes.get(position);
        Log.d(TAG, "addAppWidgetWithDesiredRecipe for selected recipe :" + selectedRecipe.getName());

        // Call directly to update the widget
//        BakingAppWidget.updateAppWidget(getApplicationContext(), appWidgetManager,
//                mAppWidgetId, selectedRecipe);
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();

        // The provider class is BakingAppWidget
        // User Intent Service to update the Widget

        AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
                getBaseContext()).getAppWidgetInfo(mAppWidgetId);

        // Finally, create the return Intent, set it with the Activity result, and finish the Activity:
        Intent startService = new Intent(WidgetConfigurationActivity.this,
                UpdateWidgetService.class);
       // startService.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);
       // startService.putExtra(BakingAppWidget.EXTRA_WIDGET_RECIPE,selectedRecipe);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppWidget.EXTRA_WIDGET_RECIPE, selectedRecipe);
        bundle.putInt(EXTRA_APPWIDGET_ID, mAppWidgetId);



        //  10-21 14:22:51.777 1601-1978/system_process
        // W/ActivityManager: Unable to start service Intent { act=CONFIGURATION ACTIVITY cmp=com.project2.bakingapplication/.utilities.BakingAppWidget$UpdateWidgetService (has extras) } U=0: not found

        startService.putExtra("bundle",bundle);
        startService.setAction("CONFIGURATION ACTIVITY");
        setResult(RESULT_OK, startService);
        startService(startService);
        finish();


    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String recipeName) {

        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, recipeName);
        prefs.commit();
    }

    /*** Implements LoaderManager's methods ***/
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
                    recipes = RecipeJsonUtils.readRecipeArray(getApplicationContext(), response);
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
                    //mLoadingIndicator.setVisibility(View.VISIBLE);
                    Log.d(TAG, "forceLoad");
                    forceLoad(); // force to load again - loadInBackground
                }
            }

            @Override
            public void deliverResult(Object data) {
                Log.d(TAG, "AsyncTaskLoader:deliverResult");
                super.deliverResult(data);
                if (data != null) {
                    mRecipes = (ArrayList<Recipe>) (data);
                    Log.d(TAG, "DeliverResult list size:" + mRecipes.size());
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader instanceof AsyncTaskLoader) {
            Log.d(TAG, "AsyncTaskLoader:onLoadFinished");
            if (data != null) {
                mRecipes = (ArrayList<Recipe>) (data);
                // TODO: init spinner
                List<String> nameList = new ArrayList<String>();
                for(Recipe r : mRecipes) {
                    nameList.add(r.getName());
                }
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, nameList);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                optionSpinner.setAdapter(nameAdapter);

            } else {
                // show error if no data
                // showErrorMessage();
                Log.e(TAG, "Unable to get Recipe data! Please check the URI link");
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    // Implements OnItemSelectedListener's methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

        Toast.makeText(adapterView.getContext(),
                "OnItemSelectedListener : " + adapterView.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();

        selectedRecipeIndex = pos;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
