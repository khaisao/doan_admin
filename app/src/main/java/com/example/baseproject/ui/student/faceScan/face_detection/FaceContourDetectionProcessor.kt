package com.example.baseproject.ui.student.faceScan.face_detection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.camera.core.ImageProxy
import com.example.baseproject.ui.student.faceScan.camerax.BaseImageAnalyzer
import com.example.baseproject.ui.student.faceScan.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceContourDetectionProcessor (
    private val view: GraphicOverlay,
    private val context: Context,
) :
    BaseImageAnalyzer<List<Face>>() {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .build()

    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    val application = context
    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect,
        image: ImageProxy
    ) {
        graphicOverlay.clear()
        results.forEach {
            val x = it.headEulerAngleX
            val y = it.headEulerAngleY
            val z = it.headEulerAngleZ

            if (-36 < x && x < -12) {
                Log.d("xxxxxxxxxx", "x DOwn")

            }
            if (-12 < x && x < 12) {
                Log.d("xxxxxxxxxx", "x Front")

            }
            if (12 < x && x < 36) {
                Log.d("xxxxxxxxxx", "x Up")
                var bitmap: Bitmap? = null
                try {
                    bitmap = image.image?.let { it1 -> convertYuv420888ImageToBitmap(it1) }
                    val cacheDir = context.cacheDir // Lấy thư mục cache của ứng dụng

                    val file = File(cacheDir, "my_image.jpg") // Thay đổi tên và định dạng tệp ảnh tùy ý

                    try {
                        val stream = FileOutputStream(file)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream) // Nếu bạn muốn sử dụng định dạng JPEG
                        // Hoặc bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) nếu bạn muốn sử dụng định dạng PNG
                        stream.flush()
                        stream.close()
                        // Tệp ảnh đã được lưu vào cache của ứng dụng
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } catch (e: java.lang.Exception) {
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                }
            }
            //For Y
            if (-36 < y && y < -12) {
                Log.d("yyyyyyyyyyyyyyyyy", "x Left")

            }
            if (-12 < y && y < 12) {
                Log.d("yyyyyyyyyyyyyyyyy", "x Frontal")

            }
            if (12 < y && y < 36) {
                Log.d("yyyyyyyyyyyyyyyyy", "x Right")

            }

            //For z
            if (-36 < z && z < -12) {
                Log.d("zzzzzzzzzz", "x Right")

            }
            if (-12 < z && z < 12) {
                Log.d("zzzzzzzzzz", "x Frontal")

            }
            if (12 < z && z < 36) {
                Log.d("zzzzzzzzzz", "x Left")

            }
            val faceGraphic = FaceContourGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(faceGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    fun convertYuv420888ImageToBitmap(image: Image): Bitmap {
        require(image.format == ImageFormat.YUV_420_888) {
            "Unsupported image format $(image.format)"
        }

        val planes = image.planes

        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        val yuvBytes = planes.map { plane ->
            val buffer = plane.buffer
            val yuvBytes = ByteArray(buffer.capacity())
            buffer[yuvBytes]
            buffer.rewind()  // Be kind…
            yuvBytes
        }

        val yRowStride = planes[0].rowStride
        val uvRowStride = planes[1].rowStride
        val uvPixelStride = planes[1].pixelStride
        val width = image.width
        val height = image.height
        @ColorInt val argb8888 = IntArray(width * height)
        var i = 0
        for (y in 0 until height) {
            val pY = yRowStride * y
            val uvRowStart = uvRowStride * (y shr 1)
            for (x in 0 until width) {
                val uvOffset = (x shr 1) * uvPixelStride
                argb8888[i++] =
                    yuvToRgb(
                        yuvBytes[0][pY + x].toIntUnsigned(),
                        yuvBytes[1][uvRowStart + uvOffset].toIntUnsigned(),
                        yuvBytes[2][uvRowStart + uvOffset].toIntUnsigned()
                    )
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(argb8888, 0, width, 0, 0, width, height)
        return bitmap
    }

    private val CHANNEL_RANGE = 0 until (1 shl 18)

    private fun yuvToRgb(nY: Int, nU: Int, nV: Int): Int {
        var nY = nY
        var nU = nU
        var nV = nV
        nY -= 16
        nU -= 128
        nV -= 128
        nY = nY.coerceAtLeast(0)

        // This is the floating point equivalent. We do the conversion in integer
        // because some Android devices do not have floating point in hardware.
        // nR = (int)(1.164 * nY + 2.018 * nU);
        // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
        // nB = (int)(1.164 * nY + 1.596 * nV);
        var nR = 1192 * nY + 1634 * nV
        var nG = 1192 * nY - 833 * nV - 400 * nU
        var nB = 1192 * nY + 2066 * nU

        // Clamp the values before normalizing them to 8 bits.
        nR = nR.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
        nG = nG.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
        nB = nB.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
        return -0x1000000 or (nR shl 16) or (nG shl 8) or nB
    }
    private fun Byte.toIntUnsigned(): Int {
        return toInt() and 0xFF
    }
    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

}