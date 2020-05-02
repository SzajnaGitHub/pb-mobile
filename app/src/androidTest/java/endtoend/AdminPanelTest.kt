package endtoend

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AdminPanelTest {

    @Test
    fun test_adminOptions() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(ViewMatchers.withId(R.id.main)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.dashboard_root)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.admin_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.admin_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.admin_console_root)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.monitoring_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.monitoring_button)).perform(ViewActions.click())
        onView(isRoot()).perform(ViewActions.pressBack());

        onView(ViewMatchers.withId(R.id.station_state_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.station_state_button)).perform(ViewActions.click())
        onView(isRoot()).perform(ViewActions.pressBack());

        onView(ViewMatchers.withId(R.id.station_prices_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.station_prices_button)).perform(ViewActions.click())
        onView(isRoot()).perform(ViewActions.pressBack());

        onView(ViewMatchers.withId(R.id.loyalty_program_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.loyalty_program_button)).perform(ViewActions.click())
        onView(isRoot()).perform(ViewActions.pressBack());
    }
}
