package com.khaipv.attendance.ui.admin.addAccount

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.RegisterAccountRequest
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_USERNAME_LOGIN
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_PASSWORD
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val addAccountActionStateChannel = Channel<AddAccountEvent>()
    val addAccountActionStateFlow = addAccountActionStateChannel.receiveAsFlow()

    fun registerAccount(userName: String, password: String, name: String, role: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.registerAccount(
                    requestAccountRequest = RegisterAccountRequest(
                        userName = userName, password = password, name = name, role = role
                    )
                )
                if (response.errors.isEmpty()) {
                    addAccountActionStateChannel.send(AddAccountEvent.AddAccountSuccess)
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

}

sealed class AddAccountEvent() {
    object AddAccountSuccess : AddAccountEvent()
    object AddAccountError : AddAccountEvent()
}

