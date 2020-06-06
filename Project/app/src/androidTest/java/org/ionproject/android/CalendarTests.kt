package org.ionproject.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.ionproject.android.calendar.jdcalendar.month
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.model.Favorite
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URI
import java.text.DateFormatSymbols
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CalendarTests {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private val db = IonApplication.db

    private val favorite = Favorite(
        id = "ID1234",
        courseAcronym = "Test",
        calendarTerm = "1920v",
        detailsUri = URI("/imagine/details/uri"),
        selfURI = URI("/imagine/self")
    )

    @Test
    fun shouldNavigateToCalendarFragment() {
        onView(withId(R.id.button_home_calendar)).perform(click())
        onView(withId(R.id.fragment_calendar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldNavigateToNextMonth() {
        onView(withId(R.id.button_home_calendar)).perform(click())
        onView(withId(R.id.fragment_calendar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val today = Calendar.getInstance()

        val months: Array<String> = DateFormatSymbols().months

        val currMonth: String = months[today.month]
        val nextMonth = months[(today.month + 1) % 12]

        onView(withId(R.id.textview_date_display_month)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    currMonth
                )
            )
        )

        onView(withId(R.id.imageview_calendar_next)).perform(click())
        onView(withId(R.id.textview_date_display_month)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    nextMonth
                )
            )
        )
    }

    // TODO: Handle URISyntaxException from mocks
    /*@Test()
    fun shouldThrowUriInvalidExceptionWhenGettingClassSectionDetails() {
        runBlocking {
            db.favoriteDao().deleteFavorite(favorite)
            db.favoriteDao().insertFavorite(favorite)
        }
        onView(withId(R.id.button_home_calendar)).perform(click())
        onView(withId(R.id.fragment_calendar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }*/

}

