package org.ionproject.android.userAPI

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchResult
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.ionwebapi.CLIENT_ID
import org.ionproject.android.userAPI.models.AuthMethod
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.SelectedMethod
import org.ionproject.android.userAPI.models.SelectedMethodResponse

class UserCredentialsViewModel(private val userAPIRepository: UserAPIRepository): ViewModel(){

    private val availableMethodsLiveData = MutableLiveData<FetchResult<List<AuthMethod>>>()

    private val loginResponseLiveData = MutableLiveData<FetchResult<SelectedMethodResponse>>()

    private val pollResponseLiveData = MutableLiveData<FetchResult<PollResponse>>()

    init{
        getAvailableAuthMethods()
    }

    private fun getAvailableAuthMethods(){
        viewModelScope.launch{

            val result = try {
                val methods = userAPIRepository.getAuthMethods()
                Log.d("API", methods.toString())
                if (methods[0].type != "") FetchSuccess(methods) else FetchFailure<List<AuthMethod>>()
            } catch (e: Exception) {
                FetchFailure<List<AuthMethod>>(e)
            }

            availableMethodsLiveData.postValue(result)
        }
    }

    fun observeAvailableAuthMethods(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<List<AuthMethod>>) -> Unit) {
        availableMethodsLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun loginWithEmail(email: String){
        viewModelScope.launch{
            val result = try {
                val response = userAPIRepository.loginWithEmail(SelectedMethod("profile", "email", CLIENT_ID, "POLL", email))
                if (response.auth_req_id != "") FetchSuccess(response) else FetchFailure<SelectedMethodResponse>()
            } catch (e: Exception) {
                FetchFailure<SelectedMethodResponse>(e)
            }

            loginResponseLiveData.postValue(result)
        }
    }

    fun observeLoginResponse(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<SelectedMethodResponse>) -> Unit) {
        loginResponseLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun pollCoreForEmailAuth(){
        viewModelScope.launch{
            val result = try {
                val response = userAPIRepository.pollCoreForAuthentication()
                if (response.access_token != "") FetchSuccess(response) else FetchFailure<PollResponse>()
            } catch (e: Exception) {
                FetchFailure<PollResponse>(e)
            }

            pollResponseLiveData.postValue(result)
        }
    }

    fun observePollResponse(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<PollResponse>) -> Unit) {
        pollResponseLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}