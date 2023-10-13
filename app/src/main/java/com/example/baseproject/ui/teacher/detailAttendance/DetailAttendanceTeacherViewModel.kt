package com.example.baseproject.ui.teacher.detailAttendance

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.DetailAttendanceStudentTeacherScreen
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAttendanceTeacherViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val rxPreferences: RxPreferences
) : BaseViewModel() {

    val allDetailAttendanceStudent =
        MutableStateFlow<List<DetailAttendanceStudentTeacherScreen>>(emptyList())

    fun getAllAttendanceSpecificSchedule(coursePerCyclesId: Int, scheduleId: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response =
                    apiInterface.getAllAttendanceSpecificSchedule(coursePerCyclesId, scheduleId)
                Log.d("asgawgawgawg", "getAllAttendanceSpecificSchedule: ${response.dataResponse}")
                if (response.errors.isEmpty()) {
                    allDetailAttendanceStudent.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }
}

