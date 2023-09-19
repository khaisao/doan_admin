package com.example.baseproject.ui.changePassword

import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val rxPreferences: RxPreferences
) : BaseViewModel() {
    private val changePasswordActionStateChannel = Channel<ChangePasswordEvent>()
    val changePasswordActionStateFlow = changePasswordActionStateChannel.receiveAsFlow()

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            changePasswordActionStateChannel.send(ChangePasswordEvent.ChangePasswordSuccess)

        }
    }

}

sealed class ChangePasswordEvent() {
    object ChangePasswordSuccess : ChangePasswordEvent()
    object ChangePasswordError : ChangePasswordEvent()
}
