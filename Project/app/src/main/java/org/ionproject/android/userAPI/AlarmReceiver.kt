package org.ionproject.android.userAPI

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchResult
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.IonApplication.Companion.preferences
import org.ionproject.android.common.IonApplication.Companion.userAPIRepository
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.TokenRefresh

/**
 * This class is used to execute the alarm code. In this case, the alarm is used to
 * refresh the access token
 */
class AlarmReceiver: BroadcastReceiver() {

    /**
     * I understand using corotines and runBlocking defeats their purpose but in this case it lets me
     * contain all of the refresh token code in this class and keeping the BroadcastReceiver contract
     * with the override methods
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Alarm", "cenas")
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        runBlocking {
            refreshAccessToken()
        }
    }

    private suspend fun refreshAccessToken(){

            val at = preferences.getAccessToken() ?: ""
            val rt = preferences.getRefreshToken()?: ""

            val response: PollResponse

            try {
                response = userAPIRepository.refreshAccessToken(TokenRefresh(at, rt))
                if (response.access_token != ""){
                    preferences.saveAccessToken(response.access_token)
                    preferences.saveRefreshToken(response.refresh_token)
                    Log.d("Alarm","accessToken: ${response.access_token}")
                    Log.d("Alarm","refreshToken: ${response.refresh_token}")
                }else{
                    Log.d("Alarm","There was an error in the if")
                    return
                }
            } catch (e: Exception) {
                Log.d("Alarm","There was an error in the catch")
                FetchFailure<PollResponse>(e)
            }
    }
}