package com.khaipv.attendance.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.khaipv.attendance.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
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

    fun login(userName: String, password: String, fcmDeviceToken: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)

                if (userName.isNotEmpty() && password.isNotEmpty() && fcmDeviceToken.isNotEmpty()) {
                    val response = apiInterface.login(userName, password, fcmDeviceToken!!)
                    if (response.errors.isEmpty()) {
                        rxPreferences.saveUserName(response.dataResponse.userName)
                        rxPreferences.savePassword(response.dataResponse.password)
                        rxPreferences.saveToken(response.dataResponse.token)
                        rxPreferences.saveRole(response.dataResponse.role)
                        rxPreferences.saveAccountId(response.dataResponse.accountId)
                        if (response.dataResponse.avatar != null) {
                            rxPreferences.saveAvatar(BuildConfig.BASE_URL + response.dataResponse.avatar)
                        } else {
                            rxPreferences.saveAvatar("")
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
                        loginActionStateChannel.send(LoginSplashEvent.LoginSuccess)
                    }
                } else {
                    loginActionStateChannel.send(LoginSplashEvent.LoginError)
                }
            } catch (throwable: Throwable) {
                onError(throwable)
                loginActionStateChannel.send(LoginSplashEvent.LoginError)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

}

sealed class LoginSplashEvent {
    object LoginSuccess : LoginSplashEvent()
    object LoginError : LoginSplashEvent()
}