package com.khaipv.attendance.ui.student.faceScan.camerax

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.khaipv.attendance.util.ImageExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay: GraphicOverlay

    companion object {
        private const val RATIO_W_H = 1
        private const val WIDTH_CROP_PERCENT = 60
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        try {
            @SuppressLint("UnsafeOptInUsageError") val image = imageProxy.image
            if (image == null) {
                imageProxy.close()
                return
            }
            var bitmap: Bitmap? = null
            try {
                bitmap = ImageExt.convertYuv420888ImageToBitmap(image)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (bitmap == null) {
                imageProxy.close()
                return
            }
            val rotationBitmap: Bitmap = com.khaipv.attendance.util.ImageUtils.rotateImage(
                bitmap, imageProxy.imageInfo.rotationDegrees
            )
//            val surfaceWidth = rotationBitmap.width.toFloat()
//            val surfaceHeight = rotationBitmap.height.toFloat()
//            val widthCrop: Float = surfaceWidth * WIDTH_CROP_PERCENT / 100
//            val heightCrop: Float = widthCrop / RATIO_W_H
//            val offsetWidth = (surfaceWidth - widthCrop) / 2
//            val offsetHeight = (surfaceHeight - heightCrop) / 2
//            val cropBitmap = Bitmap.createBitmap(
//                rotationBitmap,
//                offsetWidth.toInt(),
//                offsetHeight.toInt(),
//                widthCrop.toInt(),
//                heightCrop.toInt()
//            )
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                detectInImage(rotationBitmap)
                    .addOnSuccessListener { results ->
                        onSuccess(
                            results,
                            graphicOverlay,
                            image.cropRect,
                            imageProxy
                        )
                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        onFailure(it)
                        imageProxy.close()
                    }
            }

        } catch (e: java.lang.Exception) {
            imageProxy.close()
        }
    }


    protected abstract fun detectInImage(bitmap: Bitmap): Task<T>

    abstract fun stop()

    protected abstract fun onSuccess(
        results: T,
        graphicOverlay: GraphicOverlay,
        rect: Rect,
        image: ImageProxy
    )

    protected abstract fun onFailure(e: Exception)

}