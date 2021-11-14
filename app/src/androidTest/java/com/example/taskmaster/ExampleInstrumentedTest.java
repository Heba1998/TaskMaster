package com.example.taskmaster;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ButtonsTest() {
        onView(withText("All tasks")).check(matches(isDisplayed()));
        onView(withText("Add tasks")).check(matches(isDisplayed()));
        onView(withText("Settings")).check(matches(isDisplayed()));
    }

    @Test
    public void AddTaskTest(){
        onView(withId(R.id.add)).perform(click());
        onView((withId(R.id.titletask))).perform(typeText("Lab 31"),closeSoftKeyboard());
        onView(withId(R.id.bodytask)).perform(typeText("Add Espresso to your application."),closeSoftKeyboard());
        onView(withId(R.id.statetask)).perform(typeText("In Progress"),closeSoftKeyboard());
        onView(withId(R.id.Button3)).perform(click());
        onView(withText("Lab 31")).check(matches(isDisplayed()));
        onView(withText("Add Espresso to your application.")).check(matches(isDisplayed()));
        onView(withText("In Progress")).check(matches(isDisplayed()));
    }

    @Test
    public void RecyclerViewTest(){
        onView(withId(R.id.recycleViewId)).perform(click());
//        onView(withId(R.id.title1)).check(matches(isDisplayed()));
        onView(withId(R.id.body)).check(matches(isDisplayed()));
        onView(withId(R.id.state)).check(matches(isDisplayed()));
        onView(withId(R.id.title1)).check(matches(isDisplayed()));
    }

    @Test
    public void AllTasksTest() {
        onView(withId(R.id.all)).check(matches(isDisplayed())).perform(click());
        onView(withText("All Tasks")).check(matches(isDisplayed()));
        onView(withId(R.id.Home)).check(matches(isDisplayed())).perform(click());
    }


//    @Test
//    public void SettingTest() {
//        onView(withId(R.id.Settings)).check(matches(isDisplayed())).perform(click());
//        onView(withId(R.id.editPersonName)).check(matches(isDisplayed())).perform(typeText("Heba"));
//        onView(withId(R.id.save)).check(matches(isDisplayed())).perform(click(),closeSoftKeyboard());
//        onView(withId(R.id.GoToHomePage)).check(matches(isDisplayed())).perform(click());
//        onView(withText("Heba’s tasks")).check(matches(isDisplayed()));
//    }


     public static final String username = "Heba";
    @Test
    public void SettingTest() {
        onView(withId(R.id.Settings)).perform(click());
        onView(withId(R.id.editPersonName)).perform(typeText(username));
        onView(withId(R.id.save)).perform(click(),closeSoftKeyboard());
        onView(withId(R.id.GoToHomePage)).check(matches(isDisplayed())).perform(click());
        onView(withText(username+"’s tasks")).check(matches(isDisplayed()));
}

}