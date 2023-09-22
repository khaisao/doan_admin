package com.example.baseproject.ui.teacher.detailCourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCourseViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    fun getAllImageFromCoursePerCycle(coursePerCycleId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val response = apiInterface.getAllImageProfileStudentForCourse(coursePerCycleId)
            Log.d("asgagwawgawgawg", "getAllImageFromCoursePerCycle: ${response.dataResponse}")
        }
    }
}

