package com.khaipv.attendance.ui.student.allCourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import com.khaipv.attendance.ui.teacher.faceReco.GetImageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCourseStudentViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val allDetailCourseStudentRegister = MutableStateFlow<List<OverviewCourseStudentRegister>>(emptyList())
    val listDataImageProfileUiState = MutableStateFlow<GetDataImageProfileUiState>(GetDataImageProfileUiState.Standby)

    fun getAllCourseRegister() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.getAllCourseRegister(studentId)
                Log.d("asgagwagagwwag", "getAllCourseRegister: $response")
                if (response.errors.isEmpty()) {
                    allDetailCourseStudentRegister.value = response.dataResponse
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun getDataImageProfile(){
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                isLoading.postValue(true)
                listDataImageProfileUiState.value = GetDataImageProfileUiState.Standby
                val response = apiInterface.getDataImageProfile(rxPreferences.getStudentId())
                if(response.errors.isEmpty()){
                    listDataImageProfileUiState.value = GetDataImageProfileUiState.Success(response.dataResponse.listDataImageProfile)
                }
            }
        } catch (throwable: Throwable) {
            onError(throwable)
        } finally {
            isLoading.postValue(false)
        }
    }
}

sealed class GetDataImageProfileUiState {
    object Standby : GetDataImageProfileUiState()
    data class Success(val listDataImageProfile: List<String>) : GetDataImageProfileUiState()
    object Error : GetDataImageProfileUiState()
}

