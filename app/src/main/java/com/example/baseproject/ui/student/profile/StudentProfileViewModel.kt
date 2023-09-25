package com.example.baseproject.ui.student.profile

import androidx.lifecycle.viewModelScope
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_USERNAME_LOGIN
import com.example.core.pref.AppPreferences.Companion.PREF_PARAM_PASSWORD
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val uploadImageActionStateChannel = Channel<UploadImageEvent>()
    val uploadImageActionStateFlow = uploadImageActionStateChannel.receiveAsFlow()

    fun updateImageProfile(file: File) {
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                isLoading.postValue(true)
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    "image", file.name, requestFile
                )
                val studentIdRequestBody = "${rxPreferences.getStudentId()}".toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiInterface.updateImageProfile(studentIdRequestBody, part)
                if (response.errors.isEmpty()) {
                    uploadImageActionStateChannel.send(UploadImageEvent.UploadImageSuccess)
                }
                isLoading.postValue(false)
            }
        } catch (e: Exception) {

        } finally {

        }
    }

}

sealed class UploadImageEvent() {
    object UploadImageSuccess : UploadImageEvent()
}

