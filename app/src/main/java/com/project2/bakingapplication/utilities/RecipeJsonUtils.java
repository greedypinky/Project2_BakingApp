package com.project2.bakingapplication.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.project2.bakingapplication.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

// RecipeJsonUtils to parse the Recipe JSON
public class RecipeJsonUtils {

    private static String TAG = RecipeJsonUtils.class.getSimpleName();
    private static String JSON = "";
    private static String ID = "id";
    private static String NAME = "name";
    private static String INGREDIENTS = "ingredients";
    // ingredients's KEY
    private static String INGREDIENTS_QUANTITY = "quantity";
    private static String INGREDIENTS_MEASURE = "measure";
    private static String INGREDIENTS_INGREDIENT = "ingredient";
    private static String STEPS = "steps";
    // steps' KEY
    private static String STEP_NUM = "id"; // 0
    private static String SHORT_DESCRIPTION = "shortDescription"; // "Recipe Introduction"
    private static String DESCRIPTION  = "description"; // "Recipe Introduction"
    private static String VIDEO_URL = "videoURL"; // "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdae8_-intro-cheesecake/-intro-cheesecake.mp4"
    private static String THUMBNAIL_URL = "thumbnailURL"; // 0
    // Serving
    private static String SERVINGS = "servings";
    // image
    private static String IMAGE = "image";

    private static ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private static Hashtable<String,Recipe> hashTable = new Hashtable<String,Recipe>();

    /**
     * readRecipeArray
     * @param context
     * @return recipeList
     * @throws IOException
     */
/*
    public List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<Message>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    } */

    public static ArrayList<Recipe> readRecipeArray(Context context, String recipeJSON) throws IOException {
        recipeList = new ArrayList<Recipe>();

        // JsonReader reader = readJSONFile(context);
        JsonReader reader = readJSONFromString(recipeJSON);
        try {
            reader.beginArray();
            while (reader.hasNext()) {
               // Recipe recipe = readRecipe(reader);
               // Log.d(TAG, "read each Recipe:" + recipe.getId());
               // Log.d(TAG, "read each Recipe:" + recipe.getName());
                recipeList.add(readRecipe(reader));
            }
            reader.endArray();
        } finally {
            reader.close();
        }

        for(Recipe r: recipeList) {
            Log.d(TAG,"##### Source RecipeArray ########");
            Log.d(TAG,"readRecipeArray:" + r.getId());
            Log.d(TAG,"readRecipeArray" + r.getName());
        }

        return recipeList;
    }


    // Parse the JSON from the very first time and
    // Save in the DB ??
    private static Recipe readRecipe(JsonReader reader) {

        String id = "";
        String name = "";
        ArrayList<Ingredient> ingredients = null;
        ArrayList<Step> steps = null;
        String servings = "";
        String image = "";
        Recipe recipe = null;

        try {

            reader.beginObject();
            while(reader.hasNext()) {
                // return next Token
                String key = reader.nextName();
                if(key.equals(ID)) {
                    id = reader.nextString();
                } else if (key.equals(NAME)) {
                    name = reader.nextString();
                } else if (key.equals(INGREDIENTS)) {
                    ingredients = readIngredients(reader);
                } else if (key.equals(STEPS)) {
                   steps =  readSteps(reader);
                } else if(key.equals(SERVINGS)) {
                    servings = reader.nextString();
                } else if(key.equals(IMAGE)) {
                    image = reader.nextString();
                }

            }
            // end object
            // recipe = new Recipe(id,name,ingredients,steps,servings,image);
            Log.d(TAG,"Receipt's id:" + id);
            Log.d(TAG,"Receipt's name:" + name);
            reader.endObject();

        } catch(IOException e) {
            e.printStackTrace();
        }
        return new Recipe(id,name,ingredients,steps,servings,image);
    }

    /**
     * readIngredient
     * @param reader
     * @return ArrayList of ingredient
     */
    private static ArrayList<Ingredient> readIngredients(JsonReader reader) {

        // parse the ingredient array
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        try {
            reader.beginArray();
            // go through each Array Object
            while (reader.hasNext()) {
               Ingredient ingredient = readSingleIngredient(reader);
                ingredientList.add(ingredient);
            }

            reader.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredientList;

    }

    private static Ingredient readSingleIngredient(JsonReader reader) {

        String quantity = "";
        String measure="";
        String ingredient="";
        Ingredient ingredientObj = null;
        // parse the ingredient array

        try {
            reader.beginObject();
            // go through each Array Object
            while (reader.hasNext()) {
                // doubles.add(reader.nextDouble());
                String key = reader.nextName();
                if (key.equals(INGREDIENTS_QUANTITY)){
                    quantity = reader.nextString();
                } else if (key.equals(INGREDIENTS_MEASURE)) {
                    measure = reader.nextString();
                } else if (key.equals(INGREDIENTS_INGREDIENT)) {
                    ingredient = reader.nextString();
                }
            }

            ingredientObj = new Ingredient(quantity,measure, ingredient);

            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredientObj;

    }

    /**
     * readSteps
     * @param reader
     * @return ArrayList of Step
     */
    private static ArrayList<Step> readSteps(JsonReader reader) {
        String stepId = ""; // 0
        String shortDescription = ""; // "Recipe Introduction"
        String description = ""; // "Recipe Introduction"
        String videoURL = ""; // "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdae8_-intro-cheesecake/-intro-cheesecake.mp4"
        String thumbnailURL = ""; // 0
        ArrayList<Step> steps = new ArrayList<>();

        try {
            reader.beginArray();
            // go through each Array Object
            while (reader.hasNext()) {

                Step step =  readSingleStep(reader);
                steps.add(step);
            }

            reader.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return steps;
    }

    private static Step readSingleStep(JsonReader reader) {
        String stepId = ""; // 0
        String shortDescription = ""; // "Recipe Introduction"
        String description = ""; // "Recipe Introduction"
        String videoURL = ""; // "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdae8_-intro-cheesecake/-intro-cheesecake.mp4"
        String thumbnailURL = ""; // 0
        Step step = null;

        try {
            reader.beginObject();
            // go through each Array Object
            while (reader.hasNext()) {
                // doubles.add(reader.nextDouble());
                String key = reader.nextName();
                if (key.equals(STEP_NUM)){
                    stepId = reader.nextString();
                } else if (key.equals(SHORT_DESCRIPTION)) {
                    shortDescription = reader.nextString();
                } else if (key.equals(DESCRIPTION)) {
                    description = reader.nextString();
                } else if (key.equals(VIDEO_URL)) {
                    videoURL = reader.nextString();
                } else if (key.equals(THUMBNAIL_URL)) {
                    thumbnailURL= reader.nextString();
                }
            }

            step = new Step(stepId,shortDescription,description,videoURL,thumbnailURL);
            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return step;
    }


    /**
     * readJSONFile
     * @param context
     * @return JsonReader
     */
    private static JsonReader readJSONFile(Context context) {

        String uri = null;
        InputStream is = null;
        JsonReader reader = null;
        AssetManager assertManager = context.getAssets();
        try {

            is = assertManager.open("recipes.json");
            reader = new JsonReader(new InputStreamReader(is,"UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return reader;

    }

    /**
     * readJSONFromString
     * @param jsonStr
     * @return
     */
    private static JsonReader readJSONFromString(String jsonStr) {
        InputStream is = null;
        JsonReader reader = null;
        try {

            is = new ByteArrayInputStream(jsonStr.getBytes("UTF-8"));
            reader = new JsonReader(new InputStreamReader(is,"UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Unable to get JsonReader :" + e.getMessage());
        }

        return reader;
    }


}
