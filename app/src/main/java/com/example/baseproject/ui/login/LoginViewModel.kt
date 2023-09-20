package com.example.baseproject.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val loginActionStateChannel = Channel<LoginEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    fun login(user: String, password: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
           val response = apiInterface.login(user,password)
            Log.d("asgawgawgawg", "login: $response")
        }
    }

}

sealed class LoginEvent() {
    object LoginSuccess : LoginEvent()
    object LoginError : LoginEvent()
}
