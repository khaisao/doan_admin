package com.khaipv.attendance.ui.admin.schedule

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.CourseHaveShedule
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleAdminViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    val listCourseHaveShedule = MutableStateFlow<List<CourseHaveShedule>>(emptyList())

    fun getAllCourseHaveSchedule() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllCourseHaveSchedule()
                if (response.errors.isEmpty()) {
                    listCourseHaveShedule.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

}

