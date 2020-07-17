package org.ionproject.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.model.Favorite
import org.ionproject.android.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URI

@RunWith(AndroidJUnit4::class)
class FavoriteTests {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private val db = IonApplication.db

    private val favorite = Favorite(
        id = "ID1234",
        courseAcronym = "Test",
        calendarTerm = "1920v",
        selfURI = URI("/imagine/self")
    )


    @Test
    fun addAndDeleteFavorite() {
        runBlocking {
            db.favoriteDao().deleteFavorite(favorite)
            db.favoriteDao().insertFavorite(favorite)
        }

        onView(withId(R.id.navigation_favorites)).perform(click())
        onView(withId(R.id.fragment_favorites)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.recyclerview_favorites_classes_list)).waitForData()

        onView(withText("${favorite.courseAcronym} - ${favorite.id}")).perform(swipeRight())

        runBlocking {
            findFavoriteAndAssertWith(0)
        }
    }

    @Test
    fun undoDeleteFavorite() {
        runBlocking {
            db.favoriteDao().deleteFavorite(favorite)
            db.favoriteDao().insertFavorite(favorite)
        }

        onView(withId(R.id.navigation_favorites)).perform(click())
        onView(withId(R.id.fragment_favorites)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.recyclerview_favorites_classes_list)).waitForData()

        onView(withText("${favorite.courseAcronym} - ${favorite.id}")).perform(swipeRight())
        onView(withText("Undo")).perform(click())

        runBlocking {
            findFavoriteAndAssertWith(1)
            db.favoriteDao().deleteFavorite(favorite)
            findFavoriteAndAssertWith(0)
        }
    }

    private suspend fun findFavoriteAndAssertWith(expected: Int) {
        val count = db.favoriteDao().favoriteExists(
            course = favorite.courseAcronym,
            calendarTerm = favorite.calendarTerm,
            classSection = favorite.id
        )
        // Test if favorite has been removed from db
        assertTrue(count == expected)
    }
}