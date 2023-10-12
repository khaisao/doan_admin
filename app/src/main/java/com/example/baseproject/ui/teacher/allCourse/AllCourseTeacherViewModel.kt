package com.example.baseproject.ui.teacher.allCourse

import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.OverviewCourseTeacherAssign
import com.example.baseproject.network.ApiInterface
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

