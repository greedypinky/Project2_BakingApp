package com.project2.bakingapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe Step description adapter class
 */
public class StepDescriptionAdapter extends RecyclerView.Adapter<StepDescriptionAdapter.StepDescViewHolder> {

    private String TAG = StepDescriptionAdapter.class.getSimpleName();
    private List<Step> stepList = new ArrayList<Step>();
    private OnClickStepHandler onClickStepHandler;
    private int mCurrentStep = 0;
    private Recipe mRecipe;

    /**
     * onClickReviewHander - handle when review is clicked
     */
    public interface OnClickStepHandler {

        public void onClickToOpenDetailView(Step step, int pos);
    }

    // Constructor to pass in the call back to handle the click on the step description row
    public StepDescriptionAdapter (OnClickStepHandler handler) {
        onClickStepHandler = handler;
    }

    @Override
    public StepDescViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToRoot = false;
        View layoutView = LayoutInflater.from(context).inflate(R.layout.steps_desc_adapter_view, parent, attachToRoot);

        return new StepDescViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(StepDescViewHolder holder, int position) {
       Step stepAtPosition = stepList.get(position);
       holder.mStepDescription.setText(stepAtPosition.getShortDesc());
    }

    @Override
    public int getItemCount() {
        if(stepList != null) {
            return stepList.size();
        } else {
            return 0;
        }
    }

    // inner class for ViewHolder
    public class StepDescViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mStepDescription;

        public StepDescViewHolder(View itemView) {
            super(itemView);
            mStepDescription = (TextView)itemView.findViewById(R.id.step_desc_text);
            // must set listener
            mStepDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
              //  onClickStepHandler.onClickToOpenDetailView();
            Log.d(TAG,"Recycler View 's row onClick will trigger the call back method!!!");
            int selectedStep = getAdapterPosition(); // get the selected position
            mCurrentStep = selectedStep;
            onClickStepHandler.onClickToOpenDetailView(stepList.get(selectedStep), selectedStep);
            //Log.d(TAG, "onClick selectedTrailer position: " + selectedTrailer);
        }

       // TODO: should put this somewhere, not inside the adapter class
//        public Step getNextStep(){
//            int nextStepIndex = mCurrentStep + 1;
//            if( nextStepIndex < stepList.size()) {
//
//                return stepList.get(nextStepIndex);
//            } else {
//                // out of bound!!
//                return null;
//            }
//        }
    }

    /**
     * setData - Update the steps list
     * @param data
     */
    public void setSteps(List<Step> data) {
        if(data!=null) {
            stepList = data;
            notifyDataSetChanged();
        }
    }



}
