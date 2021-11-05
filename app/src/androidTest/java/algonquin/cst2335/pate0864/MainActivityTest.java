package algonquin.cst2335.pate0864;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Using @Test case to check if the user enters upper case letter or not
     */
    @Test
    public void testFindMissingUpperCase(){

        // Find the edittext
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        // Type the password 123#$*
        appCompatEditText.perform(replaceText("123#$*"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        // Click the button
        materialButton.perform(click());

        // Find the textview
        ViewInteraction textView = onView(withId(R.id.textView));
        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Using @Test case to check if the user enters lower case letter or not
     */
    @Test
    public void testFindMissingLowerCase(){

        // Find the edittext
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        // Type the password A@$*123
        appCompatEditText.perform(replaceText("A@$*123"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        // Click the button
        materialButton.perform(click());

        // Find the textview
        ViewInteraction textView = onView(withId(R.id.textView));
        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Using @Test case to check if the user enters digits or not
     */
    @Test
    public void testFindMissingDigits(){

        // Find the edittext
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        // Type the password Abc@$*
        appCompatEditText.perform(replaceText("Abc@$*"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        // Click the button
        materialButton.perform(click());

        // Find the textview
        ViewInteraction textView = onView(withId(R.id.textView));
        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Using @Test case to check if the user enters special characters or not
     */
    @Test
    public void testFindMissingSpecialCharacters(){

        // Find the edittext
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        // Type the password Abc123
        appCompatEditText.perform(replaceText("Abc123"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        // Click the button
        materialButton.perform(click());

        // Find the textview
        ViewInteraction textView = onView(withId(R.id.textView));
        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Using @Test case to check if the user enters special characters or not
     */
    @Test
    public void testComplexity(){

        // Find the edittext
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        // Type the password Abc@*45
        appCompatEditText.perform(replaceText("Abc@*45"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        // Click the button
        materialButton.perform(click());

        // Find the textview
        ViewInteraction textView = onView(withId(R.id.textView));
        // Check the text
        textView.check(matches(withText("Your password is complex enough!")));
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
