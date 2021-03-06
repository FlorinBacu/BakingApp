package android.example.com.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShowFirstStepInDetail {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    public void setIdeling()
    {
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }
    @After
    public void unIdeling()
    {
        IdlingRegistry.getInstance().unregister(mActivityTestRule.getActivity().getIdlingResource());
    }
    @Test
    public void showFirstStepInDetail() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list),
                        childAtPosition(
                                withId(R.id.frameLayout),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));


        ViewInteraction textView = onView(
                allOf(withId(R.id.shortdesc_step), withText("Step 1:\nRecipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_list),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Step 1:\nRecipe Introduction")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
