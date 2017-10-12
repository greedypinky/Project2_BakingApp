package com.project2.bakingapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.Step;
import com.squareup.picasso.Picasso;

/**
 * RecipeStepsFragment
 */
public class RecipeStepsFragment extends Fragment implements StepDescriptionAdapter.OnClickStepHandler{

    private String TAG = RecipeStepsFragment.class.getSimpleName();
    private TextView mRecipeTitle;
    private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepsRecyclerView;
    private StepDescriptionAdapter mStepDescAdapter;
    private IngredientAdapter mIngredientAdapter;
    private Recipe mRecipe = null;
    private StepDescriptionAdapter.OnClickStepHandler mOnClickStepHandler = null;
    private TextView mNoIngredientData;
    private TextView mNoStepData;
    private ImageView mRecipeImageView;


    private OnFragmentClickListener mOnFragmentClickListener;

    // Adapter ViewHolder's call back method
    @Override
    public void onClickToOpenDetailView(Step step, int pos) {
        mOnFragmentClickListener.onClickFragment(step, pos);
    }

    public interface OnFragmentClickListener {

        public void onClickFragment(Step step, int pos);
    }

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // inflate the fragment
        View fragmentRootView = inflater.inflate(R.layout.fragment_recipe_steps,container,false);
        mRecipeImageView = (ImageView) fragmentRootView.findViewById(R.id.recipe_image);
        mNoIngredientData = (TextView) fragmentRootView.findViewById(R.id.ingredient_no_data);
        mNoStepData = (TextView) fragmentRootView.findViewById(R.id.steps_no_data);

        mRecipeTitle = (TextView) fragmentRootView.findViewById(R.id.recipe_title);
        mIngredientRecyclerView = (RecyclerView) fragmentRootView.findViewById(R.id.recipe_ingredient_recycler_view);
        mStepsRecyclerView = (RecyclerView) fragmentRootView.findViewById(R.id.recipe_steps_recycler_view);

        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));
        mIngredientRecyclerView.setHasFixedSize(true);

        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mStepsRecyclerView.setHasFixedSize(true);

        mIngredientAdapter = new IngredientAdapter();

        // The OnClick Handler is initiated in the onAttach method - the activity must implements the callback method
        mStepDescAdapter = new StepDescriptionAdapter(this);

        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mStepsRecyclerView.setAdapter(mStepDescAdapter);

        //set data for the adapter
        if (mRecipe!=null) {
            Log.d(TAG, "Set Recipe Ingredient and step data into adapter class");
            mIngredientAdapter.setAdapterData(mRecipe.getIngredientList());
            mStepDescAdapter.setSteps(mRecipe.getStepList());
        } else {
            Log.d(TAG,"No recipe data yet!");
            // Show error text when no recipe data
//            mNoIngredientData.setVisibility(View.VISIBLE);
//            mNoStepData.setVisibility(View.VISIBLE);
//            mIngredientRecyclerView.setVisibility(View.INVISIBLE);
//            mStepsRecyclerView.setVisibility(View.INVISIBLE);
        }

        // return the fragment we just initialized!
        return fragmentRootView;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");

        if (context instanceof RecipeStepsFragment.OnFragmentClickListener) {
            mOnFragmentClickListener = (RecipeStepsFragment.OnFragmentClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepDescriptionAdapter.OnClickStepHandler");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        mOnClickStepHandler = null;
    }

    // Activity set the recipe information to fragment so that fragment can display the information
    public void setRecipe(Recipe recipe) {
        // add recipe to the fragment
        mRecipe = recipe;
        if (mStepDescAdapter!=null) {
            mStepDescAdapter.setSteps(recipe.getStepList());
        }

        if(mIngredientAdapter!=null) {
            mIngredientAdapter.setAdapterData(recipe.getIngredientList());
        }
        // set the title
        mRecipeTitle.setText(mRecipe.getName());

        // set the image if JSON data has the image URL
        setRecipeImage();

    }

    /**
     * setRecipeImage - if recipe's image url is not empty
     */
    public void setRecipeImage() {

        // add back the handle to display thumbnail image
        String imageUrl =  mRecipe.getImage();
        if (imageUrl !=null && !imageUrl.isEmpty()) {
            Uri imageUri = Uri.parse(imageUrl );
            Picasso.with(getContext()).load(imageUri).into(mRecipeImageView);
            mRecipeImageView.setVisibility(View.VISIBLE);
        }
    }
}
