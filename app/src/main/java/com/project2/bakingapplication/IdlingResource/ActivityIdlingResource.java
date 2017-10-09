package com.project2.bakingapplication.IdlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import com.project2.bakingapplication.RecipeStepsActivity;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ActivityIdlingResource
 */
public class ActivityIdlingResource implements IdlingResource {
    private RecipeStepsActivity activity;
    @Nullable
    private volatile ResourceCallback mCallback;

    // boolean for state of idle
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return RecipeStepsActivity.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {

        mCallback = callback;

    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
