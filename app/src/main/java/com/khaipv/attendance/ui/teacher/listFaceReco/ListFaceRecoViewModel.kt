package com.khaipv.attendance.ui.teacher.listFaceReco

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AttendanceBody
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFaceRecoViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    private val attendanceActionStateChannel = Channel<AttendanceEvent>()
    val attendanceActionStateFlow = attendanceActionStateChannel.receiveAsFlow()
    fun attendance(attendanceBody: AttendanceBody) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val response = apiInterface.attendance(attendanceBody)
                if(response.errors.isEmpty()){
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

