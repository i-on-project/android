package org.ionproject.android.userAPI

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.runBlocking
import org.ionproject.android.R
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.IonApplication.Companion.preferences
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.TokenRefresh

/**
 * This class is used to execute the alarm code. In this case, the alarm is used to
 * refresh the access token
 */
class AlarmReceiver : BroadcastReceiver() {

    /**
     * I understand using corotines and runBlocking defeats their purpose but in this case it lets me
     * contain all of the refresh token code in this class and keeping the BroadcastReceiver contract
     * with the override methods
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        runBlocking {
            refreshAccessToken(context)
        }
    }

    /**
     * The application uses the presence of the tokens in SharedPreferences as an indicator
     * that the user has already gone through the authentication process
     *
     * Instead of logging out the user, in case of a problem refreshing the token we just show a dia
     * logue sugesting any attempt to subscribe to a class section is goign to result in an error
     * untill the issue is resolved
     *
     * This crappy solution results of a need to maintain functionality, since the app works with
     * cached data and the lack of a refresh token only hinders the usage of the UserAPI
     */
    suspend fun refreshAccessToken(context: Context?): Boolean {

        val rt = preferences.getRefreshToken() ?: ""

        val response: PollResponse

        try {
            response = IonApplication.userAPIRepository.refreshAccessToken(TokenRefresh(rt))
            if (response.access_token != "") {
                preferences.saveAccessToken(response.access_token)
                preferences.saveRefreshToken(response.refresh_token)
                return true
            } else {
                Toast.makeText(context, R.string.error_refreshing_token, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, R.string.error_refreshing_token, Toast.LENGTH_LONG).show()
        }
        return false
    }
}