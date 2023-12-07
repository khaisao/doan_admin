package com.khaipv.attendance.ui.teacher.allAttendance

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.OverviewScheduleStudent
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllAttendanceViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    val allAttendance = MutableStateFlow<List<OverviewScheduleStudent>>(emptyList())
    fun getAllAttendanceSpecificCourse(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllAttendanceSpecificCourse(courseId)
                if (response.errors.isEmpty()) {
                    allAttendance.value = response.dataResponse
                } else {
                    messageError.postValue("Don't have attendance history")
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)

            }
        }
    }
}

