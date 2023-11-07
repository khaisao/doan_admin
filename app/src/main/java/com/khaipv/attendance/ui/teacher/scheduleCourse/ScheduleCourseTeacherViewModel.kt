package com.khaipv.attendance.ui.teacher.scheduleCourse

import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.khaipv.attendance.model.DetailAttendanceStudentTeacherScreen
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.network.ApiInterface
import com.khaipv.attendance.util.DateFormat.Companion.FORMAT_1
import com.khaipv.attendance.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScheduleCourseTeacherViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    val allSchedule = MutableStateFlow<List<DetailScheduleCourse>>(emptyList())
    fun getAllScheduleSpecificCourse(coursePerCycleId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllScheduleSpecificCourse(coursePerCycleId)
                if (response.errors.isEmpty()) {
                    allSchedule.value =
                        response.dataResponse.sortedBy { it.startTime.toDate(FORMAT_1) }
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

    suspend fun getAllAttendanceSpecificSchedule(
        coursePerCyclesId: Int,
        scheduleId: Int
    ): List<DetailAttendanceStudentTeacherScreen> {
        return withContext(Dispatchers.IO + handler) {
            apiInterface.getAllAttendanceSpecificSchedule(
                coursePerCyclesId,
                scheduleId
            ).dataResponse
        }
    }
}

