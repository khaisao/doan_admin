package com.example.baseproject.shareData

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.example.core.pref.RxPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val TAG = "ShareViewModel"

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val rxPreferences: RxPreferences,
    private val apiInterface: ApiInterface
) : BaseViewModel() {
    val listStudentRecognized = MutableStateFlow<List<AllImageProfileStudentForCourse>>(emptyList())
    val listStudentRecognizedId = mutableListOf<Int>()

}
