package com.example.baseproject.ui.teacher.allAttendance

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.OverviewScheduleStudent
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.utils.DateFormat
import com.example.core.utils.toDateWithFormatInputAndOutPut
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
    val allSchedule = MutableStateFlow<List<String>>(emptyList())
    val allStudent = MutableStateFlow<List<String>>(emptyList())
    val isDone = MutableStateFlow<Boolean>(false)
    fun getAllAttendanceSpecificCourse(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllAttendanceSpecificCourse(courseId)
                if (response.errors.isEmpty()) {
                    allAttendance.value = response.dataResponse
                    val abc = response.dataResponse[0].schedules
                    val cacheScheduleList = mutableListOf<String>()
                    for (item in abc) {
                        val timeToAdd = item.startTime.toDateWithFormatInputAndOutPut(
                            DateFormat.FORMAT_1,
                            DateFormat.FORMAT_2
                        )
                        cacheScheduleList.add(timeToAdd)
                    }
                    val cacheStudentList = mutableListOf<String>()
                    for (item in response.dataResponse) {
                        cacheStudentList.add(item.studentName)
                    }
                    allSchedule.value = cacheScheduleList
                    allStudent.value = cacheStudentList
                    isDone.value = true
                } else {
                    messageError.postValue("Dont have attendance history")
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)

            }
        }
    }
}

