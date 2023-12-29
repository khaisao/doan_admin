package com.khaipv.attendance.ui.student.allCourse

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
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
    val listDataImageProfile = MutableStateFlow<List<String>>(emptyList())

    fun getAllCourseRegister() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.getAllCourseRegister(studentId)
                if (response.errors.isEmpty()) {
                    allDetailCourseStudentRegister.value = response.dataResponse
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

