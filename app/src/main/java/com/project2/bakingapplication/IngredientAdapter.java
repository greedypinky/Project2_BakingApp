package com.project2.bakingapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project2.bakingapplication.utilities.Ingredient;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter for the ingredient's recycler view
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private String TAG = IngredientAdapter.class.getSimpleName();
    private List<Ingredient> mIngredientsList = new ArrayList<Ingredient>();

    // create the layout of the viewholder
    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean attachToRoot = false;
        View ingredientLayoutView = layoutInflater.inflate(R.layout.ingredient_adapter_view,parent, attachToRoot);

        return new IngredientViewHolder(ingredientLayoutView);
    }

    // bind the data
    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);
        // get the data based on the position
        Ingredient ingredient = mIngredientsList.get(position);
        String nameOfIngredient = ingredient.getIngredient();
        String quantity = ingredient.getQuatity();
        String measure = ingredient.getMeasure();
        String quantity_measure = quantity + " " + measure;

        Log.d(TAG, "Ingredients:" + nameOfIngredient);
        Log.d(TAG, "Ingredients:" + quantity);
        Log.d(TAG, "Ingredients:" + measure);
        // bind the data into viewHolder
        holder.mIngredientText.setText(nameOfIngredient);
        holder.mQuantityMeasure.setText(quantity + " " + measure);

    }

    @Override
    public int getItemCount() {
        if(mIngredientsList!=null) {
            return mIngredientsList.size();
        } else {
            return 0;
        }
    }

    // create a inner class for view holder
    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientText;
        private TextView mQuantityMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            // find the view from the parent view
            mIngredientText = (TextView) itemView.findViewById(R.id.ingredient_text);
            mQuantityMeasure = (TextView) itemView.findViewById(R.id.measure_text);
        }



    }

    /**
     * setAdapterData - update the ingredient list
     * @param ingredientsList
     */
    public void setAdapterData(List<Ingredient> ingredientsList){

        if(ingredientsList!=null) {
            mIngredientsList = ingredientsList;
            notifyDataSetChanged();
        }

    }
}
