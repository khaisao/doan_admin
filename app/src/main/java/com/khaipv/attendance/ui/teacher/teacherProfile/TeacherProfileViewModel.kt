package com.khaipv.attendance.ui.teacher.teacherProfile

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_USERNAME_LOGIN
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_PASSWORD
import com.example.core.pref.RxPreferences
import com.khaipv.attendance.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TeacherProfileViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
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

    private val uploadAvatarActionStateChannel = Channel<Boolean>()
    val uploadAvatarActionStateFlow = uploadAvatarActionStateChannel.receiveAsFlow()

    fun updateTeacherAvatar(file: File) {
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                isLoading.postValue(true)
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    "image", file.name, requestFile
                )
                val teacherIdRequestBody =
                    "${rxPreferences.getTeacherId()}".toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiInterface.updateTeacherAvatar(teacherIdRequestBody, part)
                if (response.errors.isEmpty()) {
                    uploadAvatarActionStateChannel.send(true)
                }
                isLoading.postValue(false)
            }
        } catch (e: Exception) {

        } finally {
            isLoading.postValue(false)
        }
    }

    private val getTeacherInfoActionStateChannel = Channel<Boolean>()
    val getTeacherInfoActionStateFlow = getTeacherInfoActionStateChannel.receiveAsFlow()

    fun getTeacherInfo() {
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response = apiInterface.getTeacherInfo(rxPreferences.getTeacherId())
                if (response.errors.isEmpty()) {
                    if (response.dataResponse.avatar != null && response.dataResponse.avatar.isNotEmpty())
                        rxPreferences.saveAvatar(BuildConfig.BASE_URL + response.dataResponse.avatar)
                    getTeacherInfoActionStateChannel.send(true)
                }
            }
        } catch (e: Exception) {

        } finally {

        }
    }

}

sealed class LogoutEvent() {
    object LogoutSuccess : LogoutEvent()
    object LogoutError : LogoutEvent()
}



