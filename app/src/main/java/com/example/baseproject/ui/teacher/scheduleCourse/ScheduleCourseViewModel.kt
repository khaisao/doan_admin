package com.example.baseproject.ui.teacher.scheduleCourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.DetailScheduleCourse
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleCourseViewModel @Inject constructor(
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
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }
}

