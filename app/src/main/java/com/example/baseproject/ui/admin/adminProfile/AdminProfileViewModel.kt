package com.example.baseproject.ui.admin.adminProfile

import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_USERNAME_LOGIN
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_PASSWORD
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val rxPreferences: RxPreferences
) : BaseViewModel() {
    private val loginActionStateChannel = Channel<LogoutEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()

    fun logOut() {
        viewModelScope.launch {
            rxPreferences.remove(PREF_PARAM_PASSWORD)
            rxPreferences.remove(PREF_PARAM_USERNAME_LOGIN)
            loginActionStateChannel.send(LogoutEvent.LogoutSuccess)
        }
    }

}

sealed class LogoutEvent() {
    object LogoutSuccess : LogoutEvent()
    object LogoutError : LogoutEvent()
}

