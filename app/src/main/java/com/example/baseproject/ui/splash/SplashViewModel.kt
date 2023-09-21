package com.example.baseproject.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.R
import com.example.baseproject.network.ApiInterface
import com.example.baseproject.ui.login.LoginEvent
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    private val loginActionStateChannel = Channel<LoginSplashEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()

    init {
        login()
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val email = rxPreferences.getEmail()
                val password = rxPreferences.getPassword()
                Log.d("asgawgawgawg", "login: $email")
                Log.d("asgawgawgawg", "login: $password")
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    val response = apiInterface.login(email, password)
                    if (response.errors.isEmpty()) {
                        rxPreferences.saveEmail(response.dataResponse.email)
                        rxPreferences.savePassword(response.dataResponse.password)
                        rxPreferences.savePassword(response.dataResponse.password)
                        rxPreferences.saveToken(response.dataResponse.token)
                        rxPreferences.saveRole(response.dataResponse.role)
                        rxPreferences.saveUserName(response.dataResponse.name)
                        loginActionStateChannel.send(LoginSplashEvent.LoginSuccess)
                    }
                } else {
                    loginActionStateChannel.send(LoginSplashEvent.LoginError)
                }
            } catch (e: Exception) {
                messageError.postValue(e)
                loginActionStateChannel.send(LoginSplashEvent.LoginError)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

}

sealed class LoginSplashEvent() {
    object LoginSuccess : LoginSplashEvent()
    object LoginError : LoginSplashEvent()
}