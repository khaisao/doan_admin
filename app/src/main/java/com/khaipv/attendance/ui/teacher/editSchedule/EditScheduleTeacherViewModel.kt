package com.khaipv.attendance.ui.teacher.editSchedule

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.khaipv.attendance.model.Classroom
import com.khaipv.attendance.model.UpdateScheduleBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScheduleTeacherViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    val allClassroom = MutableStateFlow<List<Classroom>>(emptyList())

    fun getAllClassroom() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.getAllClassroom()
                if (response.errors.isEmpty()) {
                    allClassroom.value = response.dataResponse
                }
            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }

        }
    }

    val isUpdateScheduleSuccess = MutableStateFlow<Boolean?>(null)

    fun updateSchedule(updateScheduleBody: UpdateScheduleBody) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.updateSchedule(updateScheduleBody)
                isUpdateScheduleSuccess.value = response.errors.isEmpty()
            } catch (e: Exception) {

            } finally {
                isLoading.postValue(false)
            }
        }
    }
}

