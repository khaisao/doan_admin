package com.example.baseproject.ui.splash

import android.util.Log
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
                val userName = rxPreferences.getUserName()
                val password = rxPreferences.getPassword()
                if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    val response = apiInterface.login(userName, password)
                    if (response.errors.isEmpty()) {
                        rxPreferences.saveUserName(response.dataResponse.userName)
                        rxPreferences.savePassword(response.dataResponse.password)
                        rxPreferences.saveToken(response.dataResponse.token)
                        rxPreferences.saveRole(response.dataResponse.role)
                        rxPreferences.saveAvatar(response.dataResponse.avatar)
                        if (response.dataResponse.role == 1) {
                            rxPreferences.saveStudentId(response.dataResponse.studentId)
                            rxPreferences.saveName(response.dataResponse.studentName)

                        }
                        if (response.dataResponse.role == 2) {
                            rxPreferences.saveStudentId(response.dataResponse.teacherId)
                            rxPreferences.saveName(response.dataResponse.teacherName)

                        }
                        if (response.dataResponse.role == 3) {
                            rxPreferences.saveStudentId(response.dataResponse.adminId)
                            rxPreferences.saveName(response.dataResponse.adminName)
                        }
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

sealed class LoginSplashEvent {
    object LoginSuccess : LoginSplashEvent()
    object LoginError : LoginSplashEvent()
}