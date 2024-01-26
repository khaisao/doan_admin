package com.khaipv.attendance.ui.teacher.listFaceReco

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AttendanceBody
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.khaipv.attendance.model.ApiObjectResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ListFaceRecoViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val attendanceActionStateChannel = Channel<AttendanceEvent>()
    val attendanceActionStateFlow = attendanceActionStateChannel.receiveAsFlow()
    fun attendance(listAttendanceBody: List<AttendanceBody>) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val listResponse = mutableListOf<ApiObjectResponse<Any>>()
                runBlocking {
                    for (item in listAttendanceBody) {
                        val response = apiInterface.attendance(item)
                        listResponse.add(response)
                    }
                }


                if (listResponse.all { it.errors.isEmpty() }) {
                    attendanceActionStateChannel.send(AttendanceEvent.AttendanceSuccess)
                } else {
                    attendanceActionStateChannel.send(AttendanceEvent.AttendanceError)
                }

            } catch (e: Exception) {
                messageError.postValue(e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}

sealed class AttendanceEvent() {
    object AttendanceSuccess : AttendanceEvent()
    object AttendanceError : AttendanceEvent()
}

