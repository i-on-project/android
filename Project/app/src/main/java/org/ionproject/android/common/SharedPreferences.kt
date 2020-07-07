package org.ionproject.android.common

import android.content.Context

private const val SHARED_PREFERENCES_FILE = "org.ionproject.android.SHARED_PREFERENCES_FILE"

class SharedPreferences(context: Context) {
    private val sharedPref = context.getSharedPreferences(
        SHARED_PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )

    fun getSelectedCalendarTerm(key: String) = sharedPref.getString(key, null)

    fun writeToSharePref(pair: Pair<String, String>) {
        with(sharedPref.edit()) {
            putString(pair.first, pair.second)
            commit()
        }
    }
}