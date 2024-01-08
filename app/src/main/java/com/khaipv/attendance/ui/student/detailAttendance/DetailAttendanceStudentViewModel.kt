package com.khaipv.attendance.ui.student.detailAttendance

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.DetailAttendanceStudent
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAttendanceStudentViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val allDetailAttendanceStudent = MutableStateFlow<List<DetailAttendanceStudent>>(emptyList())

    fun getDetailScheduleStudent(coursePerCyclesId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.getDetailScheduleStudent(studentId, coursePerCyclesId)
                if (response.errors.isEmpty()) {
                    allDetailAttendanceStudent.value = response.dataResponse
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            } finally {
                isLoading.postValue(false)
            }

        }
    }
}

