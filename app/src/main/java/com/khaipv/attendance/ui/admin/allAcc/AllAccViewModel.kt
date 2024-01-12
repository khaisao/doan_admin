package com.khaipv.attendance.ui.admin.allAcc

import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AccountStudentResponse
import com.khaipv.attendance.model.AccountTeacherResponse
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class AllAccViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    val listAccountTeacherResponse = MutableStateFlow<List<AccountTeacherResponse>>(emptyList())
    val listAccountStudentResponse = MutableStateFlow<List<AccountStudentResponse>>(emptyList())

    fun getAllAccount() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val accTeacherDeferred = async { getAllAccountTeacher() }
            val accStudentDeferred = async { getAllAccountStudent() }
            val accTeacherResponse = accTeacherDeferred.await()
            val accStudentResponse = accStudentDeferred.await()
            listAccountTeacherResponse.value = accTeacherResponse
            listAccountStudentResponse.value = accStudentResponse
        }
    }

    private suspend fun getAllAccountTeacher(): List<AccountTeacherResponse> {
        try {
            apiInterface.getAllAccountTeacher().let {
                if (it.errors.isEmpty()) {
                    return it.dataResponse
                } else {
                    return emptyList()
                }
            }
        } catch (throwable: Throwable) {
            return emptyList()
        }
    }

    private suspend fun getAllAccountStudent(): List<AccountStudentResponse> {
        try {
            apiInterface.getAllAccountStudent().let {
                if (it.errors.isEmpty()) {
                    return it.dataResponse
                } else {
                    return emptyList()
                }
            }
        } catch (throwable: Throwable) {
            return emptyList()
        }
    }
}

