/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package android.example.com.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.Concepts.Step;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;

import static android.example.com.bakingapp.Concepts.DataLoader.ITEM_MAP;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * This test demos a user clicking on a GridView item in MenuActivity which opens up the
 * corresponding OrderActivity.
 *
 * This test does not utilize Idling Resources yet. If idling is set in the MenuActivity,
 * then this test will fail. See the IdlingResourcesTest for an identical test that
 * takes into account Idling Resources.
 */


@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {



    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, RecipeListActivity.class);
            result.putExtra(RecipeDetailFragment.ARG_ITEM_ID,"0");
            return result;
        }
    };;

    @Before
    public void setToTest()
    {

       DataLoader.addRecipe(new DataLoader.Recipe(0,"testTitle",new ArrayList<Ingredient>(),new ArrayList<Step>(),8,"img"));



    }
    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */
    @Test
    public void RecipeTitle() {

       onView(withId(R.id.toolbar_layout)).check(matches(withText("testTitle")));


    }

}