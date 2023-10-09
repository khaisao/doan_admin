package com.example.baseproject.ui.teacher.faceReco

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.baseproject.network.ApiInterface
import com.example.core.base.BaseViewModel
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

    val imagesData = MutableStateFlow(ArrayList<Pair<String, FloatArray>>())
    var listStudentRecognized = MutableStateFlow<List<AllImageProfileStudentForCourse>>(emptyList())

    fun getAllImageFromCoursePerCycle(coursePerCycleId: Int) {
        try {
            isLoading.postValue(true)
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response = apiInterface.getAllImageProfileStudentForCourse(coursePerCycleId)
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
                    imagesData.value = images
                }
            }
        } catch (e: Exception) {

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

sealed class LoadAllImageProfileStudentEvent() {
    object LoadAllImageProfileStudentSuccess : LoadAllImageProfileStudentEvent()
    object LoadAllImageProfileStudentError : LoadAllImageProfileStudentEvent()
}

