package com.project2.bakingapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project2.bakingapplication.utilities.Recipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.squareup.picasso.Picasso;

/**
 * RecipeCardAdapter
 */
public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.CardViewHolder> {
    private String TAG = RecipeCardAdapter.class.getSimpleName();
    private List<Recipe> mRecipeList = new ArrayList<Recipe>();
    private OnClickRecipeCardListener onClickRecipeCardListener;
    private Context mContext;

    /**
     * Constructor
     * @param cardListener
     */
    public RecipeCardAdapter(OnClickRecipeCardListener cardListener) {
        onClickRecipeCardListener = cardListener;
    }


    /**
     * OnClickRecipeCardListener
     * call back to handle when the card view is clicked
     */
    public interface OnClickRecipeCardListener {

        // call back to handle whe user clicks on the recipe card
        public void onClickRecipeCard(Recipe recipe);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        mContext = parent.getContext();
        boolean attachToRoot = false;
        // Inflate the layout of view holder
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card, parent, false);
        return new CardViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder's position:" + position);
        Recipe selectedRecipe = mRecipeList.get(position);
        String recipeName = selectedRecipe.getName();
        Log.d(TAG,"onBindViewHolder's recipeName:" + selectedRecipe.getName());
        Log.d(TAG,"onBindViewHolder's recipeid:" + selectedRecipe.getId());

        holder.recipeName.setText(recipeName);
        if(!selectedRecipe.getImage().isEmpty()) {
        // Picasso will handle loading the images on a background thread, image decompression and caching the images.
           Picasso.with(mContext).load(selectedRecipe.getImage()).into(holder.recipeImageView);

        }
//        String moviePosterURL = posterBaseURL + movieItem.getPosterPath();
//        Log.d(TAG,"Load image from poster URL : " + moviePosterURL);
//        // Picasso will handle loading the images on a background thread, image decompression and caching the images.
//        Picasso.with(getContext()).load(moviePosterURL).into(posterImageView);

    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        } else {
            return 0;
        }
    }

    /**
     * CardViewHolder
     * hold the views of each card
     */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView recipeCardView;
        private TextView recipeName;
        private ImageView recipeImageView;
        public CardViewHolder(View itemView) {
            super(itemView);
            recipeCardView = (CardView)itemView.findViewById(R.id.recipe_card_view);
            recipeCardView.setOnClickListener(this);
            recipeName = (TextView)itemView.findViewById(R.id.recipe_name);
            // TODO: add back imageView
            recipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image);

        }

        @Override
        public void onClick(View view) {
            int cardIndex = getAdapterPosition();
            Recipe selectedRecipe = mRecipeList.get(cardIndex);
            onClickRecipeCardListener.onClickRecipeCard(selectedRecipe);
        }
    }

    /**
     * setAdapterData
     * @param recipes
     */
    public void setAdapterData(List<Recipe> recipes) {
        // if the data is not null
        if(recipes != null) {
            Log.d(TAG, "set recipe data:" + recipes.size());

            mRecipeList = recipes;

            for(Recipe r: recipes) {
                Log.d(TAG,"Source RecipeData");
                Log.d(TAG,"Recipe:" + r.getId());
                Log.d(TAG,"Recipe:" + r.getName());
            }

            for(Recipe r: mRecipeList) {
                Log.d(TAG,"Destination Data");
                Log.d(TAG,"Recipe:" + r.getId());
                Log.d(TAG,"Recipe:" + r.getName());
            }
            //Collections.copy(recipeList, recipes);
            notifyDataSetChanged();
        }
    }

}
