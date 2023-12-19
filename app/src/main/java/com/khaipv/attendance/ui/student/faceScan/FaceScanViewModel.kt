package com.khaipv.attendance.ui.student.faceScan

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AddImageProfileBody
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceScanViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val isScanSuccess = MutableStateFlow<Boolean?>(null)

    fun addImageProfileFaceNetModel(listImageData: List<String>) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.addImageProfile(
                    AddImageProfileBody(
                        studentId = studentId,
                        listImageData = listImageData,
                        0
                    )
                )
                if (response.errors.isEmpty()) {
                    isScanSuccess.value = true
                } else {
                    messageError.postValue("Error, try again")
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

    fun addImageProfileKbyModel(listImageData: List<String>) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.addImageProfile(
                    AddImageProfileBody(
                        studentId = studentId,
                        listImageData = listImageData,
                        modelMode = 1
                    )
                )
                if (response.errors.isEmpty()) {
                    isScanSuccess.value = true
                } else {
                    messageError.postValue("Error, try again")
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }
}

