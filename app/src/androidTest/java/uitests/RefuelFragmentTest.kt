package uitests

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.espresso.data.BackendService
import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4ClassRunner::class)
class RefuelFragmentTest {

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.distributor_view)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.car_image)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.history_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.refuel_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun test_canOpenHistoryActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.history_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.history_button)).perform(click())

        onView(withId(R.id.history_root)).check(matches(isDisplayed()))
    }

    @Test
    fun test_canPayForFuel() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))

        onView(withId(R.id.refuel_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.refuel_button)).perform(click())

        onView(withId(R.id.pay_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.pay_button)).perform(click())

        onView(withId(R.id.pay_root)).check(matches(isDisplayed()))
    }
}
