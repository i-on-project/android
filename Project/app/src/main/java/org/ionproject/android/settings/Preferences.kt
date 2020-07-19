package org.ionproject.android.settings

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREFERENCES_FILE = "org.ionproject.android.SHARED_PREFERENCES_FILE"
private const val CALENDAR_TERM_KEY = "calendar_term_key"

/**
 * Preferences class, should contain only preferences related operations.
 * The preferences are saved in a file named [SHARED_PREFERENCES_FILE] using [SharedPreferences].
 *
 * Foreach preference there should be a method get() and save(). The first is used to obtain the
 * preferences or null if the preference does not contain a value and the second to save a new value
 * to the preference.
 *
 */
class Preferences(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )

    fun getCalendarTerm() = sharedPref.getString(CALENDAR_TERM_KEY, null)

    fun saveCalendarTerm(calendarTerm: String) = with(sharedPref.edit()) {
        putString(CALENDAR_TERM_KEY, calendarTerm)
        commit()
    }
}