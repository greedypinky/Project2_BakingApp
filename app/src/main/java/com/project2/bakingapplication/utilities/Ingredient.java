package com.project2.bakingapplication.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String mQuatity;
    private String mMeasure;
    private String mIngredient;

    public Ingredient(Parcel in) {
        mQuatity = in.readString();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public Ingredient(String quality, String measure, String ingredient) {
        mQuatity = quality;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public String getQuatity() {
        return mQuatity;
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

        parcel.writeString(mQuatity);
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
}
