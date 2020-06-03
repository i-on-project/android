package org.ionproject.android

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture


/**
 * This tests validate navigation within the app.
 * First they test the bottom bar and then the rest of the application.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationTests {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun canNavigateToFavorite() {
        onView(withId(R.id.navigation_favorites)).perform(click())
        onView(withId(R.id.fragment_favorites)).check(matches(isDisplayed()))
    }

    @Test
    fun canNavigateHome() {
        onView(withId(R.id.navigation_favorites)).perform(click())
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.fragment_home)).check(matches(isDisplayed()))
    }

    @Test
    fun canNavigateToSchedule() {
        onView(withId(R.id.navigation_schedule)).perform(click())
        onView(withId(R.id.fragment_schedule)).check(matches(isDisplayed()))
    }

    /*@Test
    fun canNavigateToCalendar() {
        onView(withId(R.id.button_home_calendar)).perform(click())
        // TODO Is fragment calendar displayed
    }*/

    @Test
    fun canNavigateToCourseDetails() {
        onView(withId(R.id.button_home_programmes)).perform(click())
        val hasData = CompletableFuture<Boolean>()
        onView(withId(R.id.recyclerview_programmes_list)).check { view, noViewFoundException ->
            val adapter = (view as RecyclerView).adapter
            if (adapter != null) {
                if (adapter.itemCount > 0) {
                    hasData.complete(true)
                } else {
                    (view as RecyclerView).adapter?.registerAdapterDataObserver(
                        AdapterObserver {
                            hasData.complete(true)
                        })
                }
            }

        }
        hasData.join()
        onView(withText("LEIC")).check(matches(isDisplayed()))
        onView(withText("MEIC")).check(matches(isDisplayed()))
    }

    @Test
    fun canNavigateToCourses() {
        onView(withId(R.id.button_home_programmes)).perform(click())
        Thread.sleep(5000)
        onView(withText("LEIC")).perform(click())
        Thread.sleep(5000)
        onView(withText("1 TERM")).perform(click())
        Thread.sleep(5000)
        onView(withText("PG")).check(matches(isDisplayed()))
    }

    /*@Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("org.ionproject.android", appContext.packageName)
    }*/
}

class AdapterObserver(val onChanged2: () -> Unit) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        onChanged2()
    }

}
