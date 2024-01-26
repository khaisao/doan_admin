package com.khaipv.attendance.ui.teacher.faceReco

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.khaipv.attendance.model.AllImageProfileStudentForCourse
import com.khaipv.attendance.network.ApiInterface
import com.example.core.base.BaseViewModel
import com.khaipv.attendance.util.decodeHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class FaceRecoViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseViewModel() {

    val imagesData = MutableStateFlow<GetImageUiState>(GetImageUiState.Standby)
    var listStudentRecognized = MutableStateFlow<List<AllImageProfileStudentForCourse>>(emptyList())

    fun getAllImageFromCoursePerCycle(coursePerCycleId: Int, modelMode: Int) {
        try {
            isLoading.postValue(true)
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response =
                    apiInterface.getAllImageProfileStudentForCourse(coursePerCycleId)
                if (response.errors.isEmpty()) {
                    val data = response.dataResponse
                    listStudentRecognized.value = data
                    val images = ArrayList<Pair<String, FloatArray>>()
                    for (item in data) {
                        for (itemFloat in item.listDataImageProfile) {
                            val stringArray = itemFloat.split(",")
                            val floatArray = stringArray.map { it.toFloat() }.toFloatArray()
                            images.add(Pair(item.studentName + "_" + item.studentId, floatArray))
                        }
                    }
                    imagesData.value = GetImageUiState.Success(images)
                }
            }
        } catch (e: Exception) {
            imagesData.value = GetImageUiState.Error
        }
    }

    val imagesKbiData = MutableStateFlow<GetImageUiState>(GetImageUiState.Standby)

    fun getAllImageKbiFromCoursePerCycle(coursePerCycleId: Int) {
        try {
            isLoading.postValue(true)
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response =
                    apiInterface.getAllImageKbiProfileStudentForCourse(coursePerCycleId)
                val listUser = mutableListOf<UserWithByteArray>()
                if (response.errors.isEmpty()) {
                    val data = response.dataResponse
                    listStudentRecognized.value = data
                    for (parentItem in data) {
                        for (childItem in parentItem.listDataImageProfile) {
                            val user = UserWithByteArray(
                                name = parentItem.studentName + "_" + parentItem.studentId,
                                byteArray = childItem.decodeHex()
                            )
                            listUser.add(user)
                        }
                    }
                    imagesKbiData.value = GetImageKbiUiState.Success(listUser)
                }
                isLoading.postValue(false)
            }
        } catch (e: Exception) {
            imagesKbiData.value = GetImageKbiUiState.Error
            isLoading.postValue(false)
        } finally {
            isLoading.postValue(false)
        }
    }

    private suspend fun getBitmapFromUrl(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(url)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                null
            }
        }
    }

}

data class UserWithByteArray(
    var name: String,
    val byteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserWithByteArray

        if (name != other.name) return false
        return byteArray.contentEquals(other.byteArray)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        return result
    }
}

sealed class GetImageUiState {
    object Standby : GetImageUiState()
    data class Success(val image: ArrayList<Pair<String, FloatArray>>) : GetImageUiState()
    object Error : GetImageUiState()
}

sealed class GetImageKbiUiState {
    object Standby : GetImageUiState()
    data class Success(val image: List<UserWithByteArray>) : GetImageUiState()
    object Error : GetImageUiState()
}
