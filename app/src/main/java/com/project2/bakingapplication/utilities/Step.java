package com.project2.bakingapplication.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Step modal class
 * Single step of the recipe
 */
public class Step implements Parcelable
{

    private String mStepId;
    private String mShortDesc;
    private String mDescription;
    private String mVideoURL;
    private String mThumbNailURL;

    /**
     * Constructor
     * @param stepId
     * @param shortDesc
     * @param desc
     * @param videoURL
     * @param thumbnailURL
     */
    public Step(String stepId, String shortDesc, String desc, String videoURL, String thumbnailURL) {
        mStepId = stepId;
        mShortDesc = shortDesc;
        mDescription = desc;
        mVideoURL = videoURL;
        mThumbNailURL = thumbnailURL;
    }

    /**
     * Constructor
     * @param in
     */
    public Step(Parcel in) {
        mStepId = in.readString();
        mShortDesc = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbNailURL = in.readString();
    }

    /**
     * getStepId
     * @return id
     */
    public String getStepId() {
        return mStepId;
    }

    /**
     * getShortDesc
     * @return short description
     */
    public String getShortDesc() {
        return mShortDesc;
    }

    /**
     * getDescription
     * @return description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * getVideoURL
     * @return video URL
     */
    public String getVideoURL() {
        return mVideoURL;
    }

    /**
     * getThumbNailURL
     * @return thumbnail URL
     */
    public String getThumbNailURL() {
        return mThumbNailURL;
    }


    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {

        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int i) {

            return new Step[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mStepId);
        parcel.writeString(mShortDesc);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbNailURL);

    }
}
