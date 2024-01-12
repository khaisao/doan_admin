package com.khaipv.attendance.ui.admin.scheduleCourse

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleCourseAdminViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    val allSchedule = MutableStateFlow<List<DetailScheduleCourse>>(emptyList())
    fun getAllScheduleSpecificCourse(coursePerCycleId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllScheduleSpecificCourse(coursePerCycleId)
                if (response.errors.isEmpty()) {
                    allSchedule.value = response.dataResponse
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}

