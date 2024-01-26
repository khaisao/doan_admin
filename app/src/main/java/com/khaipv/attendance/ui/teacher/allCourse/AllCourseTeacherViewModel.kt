package com.khaipv.attendance.ui.teacher.allCourse

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.OverviewCourseTeacherAssign
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCourseTeacherViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {
    val allCourseTeacherAssign = MutableStateFlow<List<OverviewCourseTeacherAssign>>(emptyList())

    fun getAllCourseAssign() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val teacherId = rxPreferences.getTeacherId()
                val response = apiInterface.getAllCourseAssign(teacherId)
                if (response.errors.isEmpty()) {
                    allCourseTeacherAssign.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}

