package com.khaipv.attendance.ui.student.studentProfile

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
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
class StudentProfileViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    private val uploadAvatarActionStateChannel = Channel<Boolean>()
    val uploadAvatarActionStateFlow = uploadAvatarActionStateChannel.receiveAsFlow()

    fun updateStudentAvatar(file: File) {
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                isLoading.postValue(true)
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    "image", file.name, requestFile
                )
                val studentIdRequestBody =
                    "${rxPreferences.getStudentId()}".toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiInterface.updateStudentAvatar(studentIdRequestBody, part)
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

    private val getStudentInfoActionStateChannel = Channel<Boolean>()
    val getStudentInfoActionStateFlow = getStudentInfoActionStateChannel.receiveAsFlow()

    fun getStudentInfo() {
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response = apiInterface.getStudentInfo(rxPreferences.getStudentId())
                if (response.errors.isEmpty()) {
                    if (response.dataResponse.avatar != null && response.dataResponse.avatar.isNotEmpty())
                        rxPreferences.saveAvatar(BuildConfig.BASE_URL + response.dataResponse.avatar)
                    getStudentInfoActionStateChannel.send(true)
                }
            }
        } catch (e: Exception) {

        } finally {

        }
    }

}

sealed class UploadImageEvent() {
    object UploadImageSuccess : UploadImageEvent()
}

