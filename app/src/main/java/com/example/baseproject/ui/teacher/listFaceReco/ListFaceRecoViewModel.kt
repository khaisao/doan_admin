package com.example.baseproject.ui.teacher.listFaceReco

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.ApiObjectResponse
import com.example.baseproject.model.AttendanceBody
import com.example.baseproject.network.ApiInterface
import com.example.baseproject.ui.login.LoginEvent
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
    fun attendance(listAttendanceBody: List<AttendanceBody>) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                isLoading.postValue(true)
                val listResponse = mutableListOf<ApiObjectResponse<Any>>()
                for(item in listAttendanceBody){
                    val response = apiInterface.attendance(item)
                    listResponse.add(response)
                }
                val success = listResponse.all { it.errors.isEmpty() }
                if(success){
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

