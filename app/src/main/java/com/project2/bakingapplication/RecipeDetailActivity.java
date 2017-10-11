package com.project2.bakingapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project2.bakingapplication.utilities.Step;

import java.util.ArrayList;

/**
 * Detail activity when in Portrait mode
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepDetailFragment.OnClickButtonHandler {

    private static String TAG = RecipeDetailActivity.class.getSimpleName();
    private Step mCurrentStep;
    private ArrayList<Step> mSteps = new ArrayList<Step>();
    private final static String CURRENT_STEP_KEY = "currentStep";
    private final static String ALL_STEPS_KEY = "allSteps";
    private StepDetailFragment mVideoStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Get fragment to set the required data
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(TAG,"findFragmentById - start ");
        mVideoStepFragment = (StepDetailFragment) fragmentManager.findFragmentById(R.id.fragment_steps_detail);
        Log.d(TAG,"findFragmentById - end ");

        // retrieve data from the savedInstanceState after rotation
        if(savedInstanceState!=null) {
            if(savedInstanceState.containsKey(CURRENT_STEP_KEY)) {
                Log.d(TAG,"savedInstanceState - get the current step object");
                mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP_KEY);
                //mVideoStepFragment.setStepData(mCurrentStep);
            }

            if(savedInstanceState.containsKey(ALL_STEPS_KEY)) {

                mSteps = savedInstanceState.getParcelableArrayList(ALL_STEPS_KEY);
            }

        } else {
            // retrieve data from the intent
            Intent intent = getIntent();
            // Add this check to avoid exception
            if(intent != null && intent.getExtras() != null) {
                mCurrentStep = (Step) intent.getParcelableExtra("STEPS");
                //mVideoStepFragment.setStepData(mCurrentStep);
                mSteps = intent.getParcelableArrayListExtra("STEP_ARRAY");
            }
        }


            // TODO: need to set the video play into the full mode screen
            //this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            //        WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
//           getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//           getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                   WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        View detail_land = (View) mVideoStepFragment.getView().findViewById(R.id.fragment_recipe_detail_land);
//        if (detail_land != null) {
//            this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
//            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                   WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }

        Log.d(TAG,"onCreate() - set step data to fragment");
        mVideoStepFragment.setStepData(mCurrentStep);
        //mVideoStepFragment.setImage(mCurrentStep);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * onOptionsItemSelected - will be called when a toolbar menu is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // add this back to end the current activity
            case android.R.id.home:
                Log.d(TAG, "home/up button is clicked");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP_KEY, mCurrentStep);
        outState.putParcelableArrayList(ALL_STEPS_KEY, mSteps);

    }

    // Callback method to get the Previous step info
    @Override
    public Step onClickPrevious(Step currentStep) {
        // TODO: Get the previous step before the current Step
        Step previousStep = null;
        String stepId = currentStep.getStepId();
        int currentIndex = Integer.valueOf(stepId);
        // int currentIndex = mSteps.indexOf(currentStep);
        int previousIndex = currentIndex - 1;
        Log.d(TAG,"onClickPrevious index:" + (currentIndex -1));

        if(currentIndex > 0) {
           previousStep = mSteps.get(previousIndex);
        }

        // already move to the first step
//        if(previousIndex == 0) {
//            mVideoStepFragment.disablePreviousButton(true);
//        } else {
//            mVideoStepFragment.disablePreviousButton(false);
//        }
        return previousStep;
    }

    // Callback method to get the Next stop info
    @Override
    public Step onClickNext(Step currentStep) {
        Step nextStep = null;
        String stepId = currentStep.getStepId();
        int currentIndex = Integer.valueOf(stepId);
        int nextIndex = currentIndex + 1;
        // int currentIndex = mSteps.indexOf(currentStep);
        Log.d(TAG,"onClickNext index:" + (currentIndex +1));
        // Next step is available
        if(currentIndex >= 0 && currentIndex < mSteps.size()-1) {
            nextStep = mSteps.get(currentIndex + 1);
        }

        // get the last step
//        Step lastStep =  mSteps.get(mSteps.size()-1);
//        int lastIndex = Integer.valueOf(lastStep.getStepId());
//        // move to the last stop already
//        if(lastIndex == nextIndex) {
//            mVideoStepFragment.disableNextButton(true);
//        } else {
//
//            mVideoStepFragment.disableNextButton(false);
//        }

        return nextStep;
    }
}
