package com.khaipv.attendance.shareData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AllImageProfileStudentForCourse
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ShareViewModel"

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    val listStudentRecognized = MutableStateFlow<List<AllImageProfileStudentForCourse>>(emptyList())
    val listStudentRecognizedId = mutableListOf<Int>()

    private val positionBottomNavChannel = Channel<Int>()
    val positionBottomNavFlow = positionBottomNavChannel.receiveAsFlow()

    fun setPositionBottomNav(position: Int) {
        viewModelScope.launch {
            positionBottomNavChannel.send(position)
        }
    }
}
