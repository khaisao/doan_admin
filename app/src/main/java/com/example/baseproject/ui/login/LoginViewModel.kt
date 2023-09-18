package com.example.baseproject.ui.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
) : BaseViewModel() {
    private val loginActionStateChannel = Channel<LoginEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()

    fun login(user: String, password: String) {
        viewModelScope.launch {
            loginActionStateChannel.send(LoginEvent.LoginSuccess)
        }
    }

}

sealed class LoginEvent() {
    object LoginSuccess : LoginEvent()
    object LoginError : LoginEvent()

}
