package com.project2.bakingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project2.bakingapplication.utilities.Recipe;
import com.project2.bakingapplication.utilities.RecipeJsonUtils;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeCardsActivity extends AppCompatActivity implements RecipeCardAdapter.OnClickRecipeCardListener {

    private static String TAG = RecipeCardsActivity.class.getSimpleName();
    private RecyclerView mCardRecycleView;
    private RecipeCardAdapter mAdapter;
    private TextView mNoRecipeTextView;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the Card View Layout
        setContentView(R.layout.activity_recycler_view_for_recipe);

        mNoRecipeTextView = (TextView)findViewById(R.id.text_no_data_error);
        mCardRecycleView = (RecyclerView)findViewById(R.id.recipe_recycler_view);
        mCardRecycleView.setHasFixedSize(true);
        mCardRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RecipeCardAdapter(this);

        // TODO: Add back later to check the savedInstanceState otherwise init the data from JSON file
        // Get Recipe Data
        boolean initData =  initRecipeData();
        if(!initData) {
            mNoRecipeTextView.setVisibility(View.VISIBLE);
            mCardRecycleView.setVisibility(View.INVISIBLE);
        }

        mCardRecycleView.setAdapter(mAdapter);


    }

    // Implements the call back to handle when the card view is clicked
    @Override
    public void onClickRecipeCard(Recipe recipe) {
        Log.d(TAG, "onClickRecipeCard");
        Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
        intent.putExtra("RECIPE", recipe);
        startActivity(intent);
    }

    public boolean initRecipeData() {
        boolean initSuccess = false;
        try {
            recipes = RecipeJsonUtils.readRecipeArray(this);
            Log.d(TAG,"Parsed data:" + recipes);
            mAdapter.setAdapterData(recipes);
            initSuccess = true;
        } catch(IOException e) {
            e.printStackTrace();
            initSuccess = false;
        }

        return initSuccess;

    }
}
