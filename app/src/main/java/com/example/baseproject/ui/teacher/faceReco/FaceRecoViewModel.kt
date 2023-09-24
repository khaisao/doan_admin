package com.example.baseproject.ui.teacher.faceReco

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    val imagesData = MutableStateFlow(ArrayList<Pair<String, Bitmap>>())
    var listStudentRecognized = MutableStateFlow<List<AllImageProfileStudentForCourse>>(emptyList())

    fun getAllImageFromCoursePerCycle(coursePerCycleId: Int) {
        try {
            isLoading.postValue(true)
            viewModelScope.launch(Dispatchers.IO + handler) {
                val response = apiInterface.getAllImageProfileStudentForCourse(coursePerCycleId)
                if (response.errors.isEmpty()) {
                    val data = response.dataResponse
                    listStudentRecognized.value = data
                    val images = ArrayList<Pair<String, Bitmap>>()
                    for (studentItem in data) {
                        for (imageProfileItem in studentItem.listImageProfile) {
                            val bitMap =
                                getBitmapFromUrl(imageProfileItem)
                            if (bitMap != null) {
                                images.add(
                                    Pair(
                                        studentItem.name + "_" + studentItem.studentId,
                                        bitMap
                                    )
                                )
                            }
                        }
                    }
                    imagesData.emit(images)
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

