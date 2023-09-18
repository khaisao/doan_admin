package com.example.baseproject.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.R
import com.example.baseproject.ui.login.LoginEvent
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    private val loginActionStateChannel = Channel<LoginSplashEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()

    init {
        login()
    }

    fun login() {
        viewModelScope.launch {
            val email = rxPreferences.getEmail()
            val password = rxPreferences.getPassword()
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
                loginActionStateChannel.send(LoginSplashEvent.LoginSuccess)
            } else {
                loginActionStateChannel.send(LoginSplashEvent.LoginError)
            }
        }
    }

}

sealed class LoginSplashEvent() {
    object LoginSuccess : LoginSplashEvent()
    object LoginError : LoginSplashEvent()
}