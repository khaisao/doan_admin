package com.example.baseproject.ui.teacher.listFaceReco

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.baseproject.network.ApiInterface
import com.example.baseproject.ui.login.LoginEvent
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ListFaceRecoViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {

}

sealed class LoadAllImageProfileStudentEvent() {
    object LoadAllImageProfileStudentSuccess : LoadAllImageProfileStudentEvent()
    object LoadAllImageProfileStudentError : LoadAllImageProfileStudentEvent()
}

