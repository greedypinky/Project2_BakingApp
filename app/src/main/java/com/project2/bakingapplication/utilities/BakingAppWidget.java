package com.project2.bakingapplication.utilities;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.project2.bakingapplication.R;
import com.project2.bakingapplication.RecipeStepsActivity;
import com.project2.bakingapplication.RecipeWidgetService;

import java.util.List;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Implementation of App Widget functionality.
 * extends Broadcast receiver
 */
public class BakingAppWidget extends AppWidgetProvider {

    public final static String TAG = BakingAppWidget.class.getSimpleName();
    public final static String EXTRA_WIDGET_RECIPE = "widgetRecipeKey";
    //public final static String EXTRA_WIDGET_LABEL = "widgetRecipeKey";
    private static Recipe widgetRecipe;
    private static final String BUNDEL_KEY ="bundle";



    /**
     * updateAppWidget
     * call this method from Configuration Activity to pass in the desired recipe and initiate the ingredient view
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param selectedRecipe
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe selectedRecipe) {
        // Create the view layout for the ListView
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredientlistrow);

        // set recipe image
        if (!selectedRecipe.getImage().isEmpty()) {
            Uri imageUri = Uri.parse(selectedRecipe.getImage());
            remoteViews.setImageViewUri(R.id.listview_recipe_image, imageUri);
        }

        // Heading - set the name of the recipe
        remoteViews.setTextViewText(R.id.recipe_heading, selectedRecipe.getName());

        // Content - set the ingredient list
        selectedRecipe.getIngredientList();
        List<Ingredient> ingredients = selectedRecipe.getIngredientList();
        StringBuffer ingredientStrBuffer = new StringBuffer();

        for (Ingredient ingredient : ingredients) {
            String ingredientStr = ingredient.toString() + "\n";
            ingredientStrBuffer.append(ingredientStr);
        }

        remoteViews.setTextViewText(R.id.ingredient_content, ingredientStrBuffer.toString());

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

//    public static void setRecipe(Recipe recipe ){
//        widgetRecipe = recipe;
//
//    }

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
//         CharSequence widgetText = context.getString(R.string.appwidget_text);
//         Construct the RemoteViews object
//         RemoteViews views = getRecipeGridRemoteView(context);
//         RemoteViews views = getIngredientListRemoteView(context);
//         Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

//    private static RemoteViews getIngredientListRemoteView(Context context) {
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_list);
//        // Set the RecipeWidgetService2 intent to act as the adapter for the ListView
//        //Intent widgetServiceIntent = new Intent(context, RecipeWidgetService.class);
//        Intent widgetServiceIntent = new Intent(context, RecipeWidgetService.class);
//        views.setRemoteAdapter(R.id.widget_ingredient_listview, widgetServiceIntent);
//        // Handle empty view
//        views.setEmptyView(R.id.widget_ingredient_listview, R.id.widget_empty_view);
//        return views;
//    }




    /*
    private static RemoteViews getRecipeGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent widgetServiceIntent = new Intent(context, RecipeWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, widgetServiceIntent);
        // Set the DetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeStepsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty view
        views.setEmptyView(R.id.widget_grid_view, R.id.widget_empty_view);
        return views;
    } */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //RemoteViews views = getRecipeGridRemoteView(context);
            //RemoteViews views = getIngredientListRemoteView(context);
            //updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



    public static class UpdateWidgetService extends IntentService {
        public UpdateWidgetService() {
            // only for debug purpose
            super("UpdateWidgetService");

        }

        @Override
        protected void onHandleIntent(Intent intent) {
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(UpdateWidgetService.this);
            Log.e(TAG,"OnHandleIntent");
            // Get information from the intent
            Bundle bundle = intent.getBundleExtra(BUNDEL_KEY);
            if(bundle != null ) {
                int incomingAppWidgetId = bundle.getInt(EXTRA_APPWIDGET_ID,
                        INVALID_APPWIDGET_ID);
                Recipe desiredRecipe = (Recipe) bundle.getParcelable(EXTRA_WIDGET_RECIPE);

//            int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
//                    INVALID_APPWIDGET_ID);
//
//            Recipe desiredRecipe  = (Recipe) intent.getParcelableExtra(EXTRA_WIDGET_RECIPE);


                if (incomingAppWidgetId != INVALID_APPWIDGET_ID) {
                    try {
                        Log.e(TAG, "OnHandleIntent - updateAppWidget");
                        updateAppWidget(getApplicationContext(), appWidgetManager,
                                incomingAppWidgetId, desiredRecipe);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error occurs:" + e.getMessage());
                    }

                } else {
                    Log.e(TAG, "OnHandleIntent - INVALID APP WIDGET ID");
                }

            }  else {
                Log.e(TAG, "!!!OnHandleIntent - unable to get Extra");
            }

        }

    }


}

