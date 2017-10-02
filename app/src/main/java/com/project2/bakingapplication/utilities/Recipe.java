package com.project2.bakingapplication.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe Modal class
 */
public class Recipe implements Parcelable {

    private static String mId = null;
    private static String mName = null;
    private static List<Ingredient> mIngredientList = new ArrayList<Ingredient>();
    private static List<Step> mStepList = new ArrayList<Step>();


    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            // create recipe with a constructor that takes in parcel parameter
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i)
        {
            return new Recipe[i];
        }
    };

    // Serving
    private String mServings = null;
    // image
    private String mImage = null;

    /**
     * Constructor
     */
    public Recipe() {

    }

    /**
     * Constructor
     * @param in
     */
    public Recipe(Parcel in) {
        // read in all the properties
        mId = in.readString();
        mName = in.readString();
        in.readTypedList(mIngredientList, Ingredient.CREATOR);
        in.readTypedList(mStepList, Step.CREATOR);
        mServings = in.readString();
        mImage = in.readString();
    }

    /**
     * constructor
     * @param id
     * @param name
     * @param ingredients
     * @param steps
     * @param servings
     * @param image
     */
    public Recipe(String id, String name, List<Ingredient> ingredients, List<Step> steps,String servings, String image) {

        mId = id;
        mName = name;
        mIngredientList = (ArrayList)ingredients;
        mStepList = (ArrayList) steps;
        mServings = servings;
        mImage = image;
    }


    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<Ingredient> getIngredientList() {
        return mIngredientList;
    }

    public List<Step> getStepList() {
        return mStepList;
    }

    public String getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        // write out the properties
        out.writeString(mId);
        out.writeString(mName);
        out.writeTypedList(mIngredientList);
        out.writeTypedList(mStepList);
        out.writeString(mServings);
        out.writeString(mImage);

    }
}
