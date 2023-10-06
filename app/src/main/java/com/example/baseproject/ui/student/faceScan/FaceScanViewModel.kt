package com.example.baseproject.ui.student.faceScan

import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.AddImageProfileBody
import com.example.baseproject.network.ApiInterface
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

    fun addImageProfile(listImageData: List<String>) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.addImageProfile(
                    AddImageProfileBody(
                        studentId = studentId,
                        listImageData = listImageData
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

