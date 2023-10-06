package com.example.baseproject.ui.student.schedule

import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.CourseStudentRegister
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleStudentViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val allCourseStudentRegister = MutableStateFlow<List<CourseStudentRegister>>(emptyList())
    val listDataImageProfile = MutableStateFlow<List<String>>(emptyList())

    fun getAllCourseRegister() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.getAllCourseRegister(studentId)
                if (response.errors.isEmpty()) {
                    allCourseStudentRegister.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

    fun getDataImageProfile(){
        try {
            viewModelScope.launch(Dispatchers.IO + handler) {
                isLoading.postValue(true)
                val response = apiInterface.getDataImageProfile(rxPreferences.getStudentId())
                if(response.errors.isEmpty()){
                    listDataImageProfile.value = response.dataResponse.listDataImageProfile
                }
            }
        } catch (e: Exception) {

        } finally {
            isLoading.postValue(false)
        }
    }
}

