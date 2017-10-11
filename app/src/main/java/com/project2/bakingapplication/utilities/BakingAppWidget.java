package com.project2.bakingapplication.utilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.project2.bakingapplication.R;
import com.project2.bakingapplication.RecipeStepsActivity;
import com.project2.bakingapplication.RecipeWidgetService;
import com.project2.bakingapplication.RecipeWidgetService2;

/**
 * Implementation of App Widget functionality.
 * extends Broadcast receiver
 */
public class BakingAppWidget extends AppWidgetProvider {

    public final static String EXTRA_WIDGET_LABEL = "widgetRecipeKey";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        // RemoteViews views = getRecipeGridRemoteView(context);
        RemoteViews views = getIngredientListRemoteView(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getIngredientListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_list);
        // Set the RecipeWidgetService2 intent to act as the adapter for the ListView
        Intent widgetServiceIntent = new Intent(context, RecipeWidgetService.class);
        views.setRemoteAdapter(R.id.widget_ingredient_listview, widgetServiceIntent);
        // Set the DetailActivity intent to launch when clicked
        // Intent appIntent = new Intent(context, RecipeStepsActivity.class);
//        Intent appIntent = new Intent();
//        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.widget_ingredient_listview, appPendingIntent);
        // Handle empty view
       // views.setEmptyView(R.id.widget_ingredient_listview, R.id.widget_empty_view);
        return views;
    }

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
            RemoteViews views = getIngredientListRemoteView(context);
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
}

