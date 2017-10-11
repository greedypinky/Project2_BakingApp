package com.project2.bakingapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.project2.bakingapplication.utilities.Ingredient;
import com.project2.bakingapplication.utilities.NetworkUtils;
import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.RecipeJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RecipeWidgetService2 extends RemoteViewsService {

    // Constructor
    public RecipeWidgetService2() {

        super();
    }
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory2(this.getApplicationContext());
    }


}

class RecipeRemoteViewFactory2 implements RemoteViewsService.RemoteViewsFactory {

    private static String TAG = RecipeRemoteViewFactory.class.getSimpleName();
    Context mContext;
    private ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    // Constructor
    public RecipeRemoteViewFactory2(Context applicationContext) {

        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d("RecipeRemoteViewFactory", "onCreate()");
    }

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

    }

    @Override
    public int getCount() {

        if (mRecipes!=null && mRecipes.size() > 0) {
            return mRecipes.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // display the recipe's ingredient list on the ListView
        Recipe selectedRecipe;
        if (mRecipes!=null && mRecipes.size() > 0) {

            selectedRecipe = mRecipes.get(position);
            // Create the view layout for the ListView
           // RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredientlistrow);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_layout);
            // set recipe image
//            if (!selectedRecipe.getImage().isEmpty()) {
//                Uri imageUri = Uri.parse(selectedRecipe.getImage());
//                remoteViews.setImageViewUri(R.id.listview_recipe_image, imageUri);
//            }

            // Heading - set the name of the recipe
            remoteViews.setTextViewText(R.id.recipe_heading, selectedRecipe.getName());

            // Content - set the ingredient list
//            selectedRecipe.getIngredientList();
//            List<Ingredient> ingredients = selectedRecipe.getIngredientList();
//            StringBuffer ingredientStrBuffer = new StringBuffer();
//
//            for (Ingredient ingredient : ingredients) {
//                String ingredientStr = ingredient.toString() + "\n";
//                ingredientStrBuffer.append(ingredientStr);
//            }
//
//            remoteViews.setTextViewText(R.id.ingredient_content, ingredientStrBuffer.toString());


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
