package com.example.baseproject.ui.student.faceScan.camerax

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay: GraphicOverlay

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            detectInImage(InputImage.fromMediaImage(
                it, imageProxy.imageInfo.rotationDegrees))
                .addOnSuccessListener { results ->
                    Log.d("asgawgawgwagawg", "analyze: ${imageProxy.imageInfo.rotationDegrees}")
                    Log.d("agawggwaawgwag", "analyze: $results")
                    onSuccess(
                        results,
                        graphicOverlay,
                        it.cropRect,
                        imageProxy
                    )
                    imageProxy.close()
                }
                .addOnFailureListener {
                    onFailure(it)
                    imageProxy.close()
                }
        }
    }

    protected abstract fun detectInImage(image: InputImage): Task<T>

    abstract fun stop()

    protected abstract fun onSuccess(
        results: T,
        graphicOverlay: GraphicOverlay,
        rect: Rect,
        image: ImageProxy
    )

    protected abstract fun onFailure(e: Exception)

}