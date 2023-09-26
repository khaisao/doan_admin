package com.example.baseproject.ui.login

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
class LoginViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val loginActionStateChannel = Channel<LoginEvent>()
    val loginActionStateFlow = loginActionStateChannel.receiveAsFlow()

    fun login(user: String, password: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.login(user, password)
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
                    loginActionStateChannel.send(LoginEvent.LoginSuccess)
                }
            } catch (e: Exception) {
                messageError.postValue(e)
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
