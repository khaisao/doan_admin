package com.example.baseproject.ui.student.detailSchedule

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.CourseStudentRegister
import com.example.baseproject.model.DetailScheduleStudent
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScheduleStudentViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val allDetailScheduleStudent = MutableStateFlow<List<DetailScheduleStudent>>(emptyList())

    fun getDetailScheduleStudent(coursePerCyclesId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val studentId = rxPreferences.getStudentId()
                val response = apiInterface.getDetailScheduleStudent(studentId, coursePerCyclesId)
                if (response.errors.isEmpty()) {
                    allDetailScheduleStudent.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }
}

