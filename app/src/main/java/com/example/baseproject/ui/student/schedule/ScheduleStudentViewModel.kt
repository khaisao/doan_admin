package com.example.baseproject.ui.student.schedule

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleStudentViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    fun getAllImageFromCoursePerCycle(coursePerCycleId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val response = apiInterface.getAllImageProfileStudentForCourse(coursePerCycleId)
            Log.d("asgagwawgawgawg", "getAllImageFromCoursePerCycle: ${response.dataResponse}")
        }
    }
}

