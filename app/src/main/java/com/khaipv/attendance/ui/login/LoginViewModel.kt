package com.khaipv.attendance.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun login(user: String, password: String, fcmDeviceToken: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.login(user, password, fcmDeviceToken)
                Log.d("sagagwaggawagwga", "login: $response")
                if (response.errors.isEmpty()) {
                    rxPreferences.saveUserName(response.dataResponse.userName)
                    rxPreferences.savePassword(response.dataResponse.password)
                    rxPreferences.saveToken(response.dataResponse.token)
                    rxPreferences.saveRole(response.dataResponse.role)
                    rxPreferences.saveAccountId(response.dataResponse.accountId)
                    if (response.dataResponse.avatar != null) {
                        rxPreferences.saveAvatar(response.dataResponse.avatar)
                    }
                    if (response.dataResponse.role == 1) {
                        rxPreferences.saveStudentId(response.dataResponse.studentId)
                        rxPreferences.saveName(response.dataResponse.studentName)

                    }
                    if (response.dataResponse.role == 2) {
                        rxPreferences.saveTeacherId(response.dataResponse.teacherId)
                        rxPreferences.saveName(response.dataResponse.teacherName)

                    }
                    if (response.dataResponse.role == 3) {
                        rxPreferences.saveAdminId(response.dataResponse.adminId)
                        rxPreferences.saveName(response.dataResponse.adminName)
                    }
                    loginActionStateChannel.send(LoginEvent.LoginSuccess)
                }
            } catch (throwable: Throwable) {
                onError(throwable)
                loginActionStateChannel.send(LoginEvent.LoginError)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

}

sealed class LoginEvent() {
    object LoginSuccess : LoginEvent()
    object LoginError : LoginEvent()
}
