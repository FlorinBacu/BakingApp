package android.example.com.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by florinel on 09.03.2018.
 */
@RunWith(AndroidJUnit4.class)
public class ShowRecipeTest {
    public IntentsTestRule<RecipeDetailActivity> mActivityRule = new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.

        intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }
    @Test
    public void TestShowIntededRecipe()
    {
        onData(anything()).inAdapterView(withId(R.id.recipe_list)).atPosition(0).perform(click());
       intended(allOf(hasExtra(RecipeDetailFragment.ARG_ITEM_ID,"0")));
    }


}
