package android.example.com.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by florinel on 09.03.2018.
 */
@RunWith(AndroidJUnit4.class)
public class ShowStepTest {
    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<StepActivity>(StepActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, StepActivity.class);
            result.putExtra("videoURL","videoURL");
            result.putExtra("desc","desc");
            return result;
        }
    };;

    @Test
    public void TestShowIntededStep()
    {
        onView(withId(R.id.desc_step)).check(matches(withText("desc")));
    }


}
