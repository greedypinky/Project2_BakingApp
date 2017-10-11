package com.project2.bakingapplication.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredient(Parcel in) {
        mQuantity = in.readString();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public Ingredient(String quality, String measure, String ingredient) {
        mQuantity = quality;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);

    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel parcel) {

            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {

            return new Ingredient[i];
        }
    };

    @Override
    public String toString() {
        String ingredientStr = mQuantity + " " + mMeasure + " " + mIngredient;
        return  ingredientStr;
    }
}
