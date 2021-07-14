package org.ionproject.android.settings

import android.content.Context
import android.content.SharedPreferences
import org.ionproject.android.common.ionwebapi.WEB_API_HOST

private const val SHARED_PREFERENCES_FILE = "org.ionproject.android.SHARED_PREFERENCES_FILE"
private const val CALENDAR_TERM_KEY = "calendar_term_key"
private const val WEB_API_HOST_KEY = "webApi_host_key"
private const val USER_API_ACCESS_TOKEN_KEY = "access_token_key"
private const val USER_API_REFRESH_TOKEN_KEY = "refresh_token_key"

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

    fun getWebApiHost() = sharedPref.getString(WEB_API_HOST_KEY, WEB_API_HOST)

    fun saveWebApiHost(newUrl: String) = with(sharedPref.edit()) {
        putString(WEB_API_HOST_KEY, newUrl)
        commit()
    }

    fun saveAccessToken(token: String) = with(sharedPref.edit()){
        putString(USER_API_ACCESS_TOKEN_KEY, token)
        commit()
    }

    fun getAccessToken() = sharedPref.getString(USER_API_ACCESS_TOKEN_KEY, "")

    fun saveRefreshToken(token: String) = with(sharedPref.edit()){
        putString(USER_API_REFRESH_TOKEN_KEY, token)
        commit()
    }

    fun getRefreshToken() = sharedPref.getString(USER_API_REFRESH_TOKEN_KEY, "")
}