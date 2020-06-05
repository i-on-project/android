package org.ionproject.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.ionproject.android.course_details.ClassesListAdapter
import org.ionproject.android.courses.CoursesListAdapter
import org.ionproject.android.programmeDetails.TermsListAdapter
import org.ionproject.android.programmes.ProgrammesListAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
        // Navigate to programmes fragment
        onView(withId(R.id.button_home_programmes)).perform(click())
        onView(withId(R.id.fragment_programmes)).check(matches(isDisplayed()))

        // Navigate to programme details
        onView(withId(R.id.recyclerview_programmes_list)).waitForData().perform(
            RecyclerViewActions.actionOnItemAtPosition<ProgrammesListAdapter.ProgrammeViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fragment_programme_details)).check(matches(isDisplayed()))

        // Navigate to courses
        onView(withId(R.id.recyclerview_programme_details_terms)).waitForData().perform(
            RecyclerViewActions.actionOnItemAtPosition<TermsListAdapter.TermViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fragment_courses)).check(matches(isDisplayed()))

        // Navigate to course details
        onView(withId(R.id.recyclerview_courses_list)).waitForData().perform(
            RecyclerViewActions.actionOnItemAtPosition<CoursesListAdapter.CourseViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fragment_course_details)).check(matches(isDisplayed()))

        // Navigate to class section
        onView(withId(R.id.recyclerview_course_details_classes_list)).waitForData().perform(
            RecyclerViewActions.actionOnItemAtPosition<ClassesListAdapter.ClassViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fragment_class_section)).check(matches(isDisplayed()))
    }
}


