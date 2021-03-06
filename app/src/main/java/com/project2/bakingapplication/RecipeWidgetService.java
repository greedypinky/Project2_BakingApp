package com.project2.bakingapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.project2.bakingapplication.utilities.BakingAppWidget;
import com.project2.bakingapplication.utilities.Ingredient;
import com.project2.bakingapplication.utilities.NetworkUtils;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.RecipeJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecipeWidgetService extends RemoteViewsService {



    public RecipeWidgetService() {

        super();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory(this.getApplicationContext());
    }
}

/**
 * extend the RemoteViewsService to provide the appropriate RemoteViewsFactory's used to populate the remote collection view (GridView)
 */
class RecipeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = RecipeRemoteViewFactory.class.getSimpleName();
    Context mContext;
    private ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    // Constructor
    public RecipeRemoteViewFactory(Context applicationContext) {

        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.e("RecipeRemoteViewFactory", "onCreate()");

    }

    // called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        Log.d("RecipeRemoteViewFactory", "onDataSetChanged()");
        URL recipeURL = NetworkUtils.getRecipeURL();
        try {

            String response = NetworkUtils.getResponseFromHttp(recipeURL);
            Log.d("RecipeRemoteViewFactory", "AsyncTaskLoader - HTTP Response:" + response);
            mRecipes = RecipeJsonUtils.readRecipeArray(mContext, response);
            Log.d("RecipeRemoteViewFactory", "Get the Recipe Object !!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RecipeRemoteViewFactory", e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        Log.e("RecipeRemoteViewFactory", "onDestroy()");

    }

    @Override
    public int getCount() {
        if(mRecipes!=null && mRecipes.size() > 0) {
            return mRecipes.size();
        } else {
            return 0;
        }
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
 //   @Override
//    public RemoteViews getViewAt(int position) {
//        // TODO: can display the recipe name on the GRID view
//        Recipe selectedRecipe;
//        if (mRecipes!=null && mRecipes.size() > 0) {
//
//            selectedRecipe =  mRecipes.get(position);
//            // Create the view layout for the GridView
//            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_layout);
//            remoteViews .setTextViewText(R.id.widget_recipe_text, selectedRecipe.getName());
//
//            // Set List of Ingredient into the Widget ListView
//            String name = selectedRecipe.getName();
//            selectedRecipe.getIngredientList();
//            List<Ingredient> ingredients = selectedRecipe.getIngredientList();
//
//            if (!selectedRecipe.getImage().isEmpty()) {
//                //Uri imageUri = Uri.parse(selectedRecipe.getImage());
//                //views.setImageViewUri(R.id.widget_recipe_image, imageUri);
//            } else {
//
//                // TODO: we can add a place holder bitmap image
//            }
//
//
//            Intent fillInIntent = new Intent();
//            fillInIntent.putExtra(RecipeStepsActivity.WIDGET_RECIPE_KEY, selectedRecipe);
//            remoteViews.setOnClickFillInIntent(R.id.widget_recipe_root, fillInIntent);
//            return remoteViews;
//        } else {
//            return null;
//        }
//    }

    @Override
    public RemoteViews getViewAt(int position) {
        // display the recipe's ingredient list on the ListView
        Recipe selectedRecipe;
        if (mRecipes!=null && mRecipes.size() > 0) {

            selectedRecipe = mRecipes.get(position);
            // Create the view layout for the ListView
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredientlistrow);

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
            return remoteViews;
        } else {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


