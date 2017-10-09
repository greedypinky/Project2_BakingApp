package com.project2.bakingapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BakingAppInstrumentedTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeCardsActivity> mActivityTestRule =
            new ActivityTestRule(RecipeCardsActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }


    @Test
    public void clickCardViewItem_OpensActivity() {

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert ingredient/steps view is displayed
        onView(withId(R.id.textview_recipe_ingredient)).check(matches(withText(R.string.ingredients_label)));
        onView(withId(R.id.textview_steps)).check(matches(withText(R.string.steps_label)));

        // Click on the first step to see the instruction
        onView(withId(R.id.recipe_steps_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert landing on the 1st step instruction page
        // onView(withId(R.id.recipe_step_video)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_step_instructions)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_step_instructions)).check(matches(withText("Recipe Introduction")));

        // if test with a phone we can also assert the buttons
        onView(withId(R.id.button_next)).perform(ViewActions.scrollTo()).check(matches(isClickable()));
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.button_previous)).perform(ViewActions.scrollTo()).check(matches(isClickable()));
        onView(withId(R.id.button_previous)).perform(click());

    }


}
