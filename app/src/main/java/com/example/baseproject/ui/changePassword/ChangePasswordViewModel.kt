package com.example.baseproject.ui.changePassword

import androidx.lifecycle.viewModelScope
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val changePasswordActionStateChannel = Channel<ChangePasswordEvent>()
    val changePasswordActionStateFlow = changePasswordActionStateChannel.receiveAsFlow()

    fun changePassword(newPassword: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response =
                    apiInterface.changePassword(rxPreferences.getAccountId(), newPassword)
                if (response.errors.isEmpty()) {
                    rxPreferences.savePassword(newPassword)
                    changePasswordActionStateChannel.send(ChangePasswordEvent.ChangePasswordSuccess)
                } else {
                    messageError.postValue("Have error")
                    changePasswordActionStateChannel.send(ChangePasswordEvent.ChangePasswordError)
                }
            } catch (e: Exception) {
                messageError.postValue(e)

                changePasswordActionStateChannel.send(ChangePasswordEvent.ChangePasswordError)

            } finally {
                isLoading.postValue(false)
            }

        }
    }

}

sealed class ChangePasswordEvent() {
    object ChangePasswordSuccess : ChangePasswordEvent()
    object ChangePasswordError : ChangePasswordEvent()
}
