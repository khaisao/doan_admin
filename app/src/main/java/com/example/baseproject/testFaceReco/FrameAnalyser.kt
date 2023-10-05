package com.example.baseproject.testFaceReco

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.baseproject.model.faceReco.FaceNetModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.ml.quaterion.facenetdetection.Prediction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

// Analyser class to process frames and produce detections.
class FrameAnalyser(
    context: Context,
    private var boundingBoxOverlay: BoundingBoxOverlay,
    private var model: FaceNetModel
) : ImageAnalysis.Analyzer {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
    private val detector = FaceDetection.getClient(realTimeOpts)

    private val nameScoreHashmap = HashMap<String, ArrayList<Float>>()
    private var subject = FloatArray(model.embeddingDim)

    // Used to determine whether the incoming frame should be dropped or processed.
    private var isProcessing = false

    // Store the face embeddings in a ( String , FloatArray ) ArrayList.
    // Where String -> name of the person and FloatArray -> Embedding of the face.
    var faceList = ArrayList<Pair<String, FloatArray>>()

    // <-------------- User controls --------------------------->

    // Use any one of the two metrics, "cosine" or "l2"
    private val metricToBeUsed = "l2"

    // Use this variable to enable/disable mask detection.
    private val isMaskDetectionOn = false

    // <-------------------------------------------------------->


    init {
        boundingBoxOverlay.drawMaskLabel = isMaskDetectionOn
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        // If the previous frame is still being processed, then skip this frame
        if (isProcessing || faceList.size == 0) {
            image.close()
            return
        } else {
            isProcessing = true

            // Rotated bitmap for the FaceNet model
            val cameraXImage = image.image!!
            var frameBitmap = Bitmap.createBitmap(
                cameraXImage.width,
                cameraXImage.height,
                Bitmap.Config.ARGB_8888
            )
            frameBitmap.copyPixelsFromBuffer(image.planes[0].buffer)
            frameBitmap =
                BitmapUtils.rotateBitmap(frameBitmap, image.imageInfo.rotationDegrees.toFloat())
            //val frameBitmap = BitmapUtils.imageToBitmap( image.image!! , image.imageInfo.rotationDegrees )

            // Configure frameHeight and frameWidth for output2overlay transformation matrix.
            if (!boundingBoxOverlay.areDimsInit) {
                boundingBoxOverlay.frameHeight = frameBitmap.height
                boundingBoxOverlay.frameWidth = frameBitmap.width
            }

            val inputImage = InputImage.fromBitmap(frameBitmap, 0)
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    CoroutineScope(Dispatchers.IO).launch {
                        runModel(faces, frameBitmap)
                    }
                }
                .addOnCompleteListener {
                    image.close()
                }
        }
    }


    private suspend fun runModel(faces: List<Face>, cameraFrameBitmap: Bitmap) {
        withContext(Dispatchers.Default) {
            val predictions = ArrayList<Prediction>()
            for (face in faces) {
                try {
                    // Crop the frame using face.boundingBox.
                    // Convert the cropped Bitmap to a ByteBuffer.
                    // Finally, feed the ByteBuffer to the FaceNet model.
                    val croppedBitmap =
                        BitmapUtils.cropRectFromBitmap(cameraFrameBitmap, face.boundingBox)
                    subject = model.getFaceEmbedding(croppedBitmap)

                    // Perform face mask detection on the cropped frame Bitmap.
                    val maskLabel = ""

                    // Continue with the recognition if the user is not wearing a face mask
                    // Perform clustering ( grouping )
                    // Store the clusters in a HashMap. Here, the key would represent the 'name'
                    // of that cluster and ArrayList<Float> would represent the collection of all
                    // L2 norms/ cosine distances.
                    for (i in 0 until faceList.size) {
                        // If this cluster ( i.e an ArrayList with a specific key ) does not exist,
                        // initialize a new one.
                        if (nameScoreHashmap[faceList[i].first] == null) {
                            // Compute the L2 norm and then append it to the ArrayList.
                            val p = ArrayList<Float>()
                            if (metricToBeUsed == "cosine") {
                                p.add(cosineSimilarity(subject, faceList[i].second))
                            } else {
                                p.add(L2Norm(subject, faceList[i].second))
                            }
                            nameScoreHashmap[faceList[i].first] = p
                        }
                        // If this cluster exists, append the L2 norm/cosine score to it.
                        else {
                            if (metricToBeUsed == "cosine") {
                                nameScoreHashmap[faceList[i].first]?.add(
                                    cosineSimilarity(
                                        subject,
                                        faceList[i].second
                                    )
                                )
                            } else {
                                nameScoreHashmap[faceList[i].first]?.add(
                                    L2Norm(
                                        subject,
                                        faceList[i].second
                                    )
                                )
                            }
                        }
                    }

                    // Compute the average of all scores norms for each cluster.
                    val avgScores = nameScoreHashmap.values.map { scores ->
                        scores.toFloatArray().average()
                    }

                    val names = nameScoreHashmap.keys.toTypedArray()
                    nameScoreHashmap.clear()

                    // Calculate the minimum L2 distance from the stored average L2 norms.
                    val bestScoreUserName: String = if (metricToBeUsed == "cosine") {
                        // In case of cosine similarity, choose the highest value.
                        if (avgScores.maxOrNull()!! > model.model.cosineThreshold) {
                            names[avgScores.indexOf(avgScores.maxOrNull()!!)]
                        } else {
                            "Unknown"
                        }
                    } else {
                        // In case of L2 norm, choose the lowest value.
                        if (avgScores.minOrNull()!! > model.model.l2Threshold) {
                            "Unknown"
                        } else {
                            names[avgScores.indexOf(avgScores.minOrNull()!!)]
                        }
                    }
                    Logger.log("Person identified as $bestScoreUserName")
                    predictions.add(
                        Prediction(
                            face.boundingBox,
                            bestScoreUserName,
                            maskLabel
                        )
                    )
                    if (bestScoreUserName != "Unknown") {
                        listStudentDetect.add(bestScoreUserName)
                    }
                } catch (e: Exception) {
                    // If any exception occurs with this box and continue with the next boxes.
                    Log.e("Model", "Exception in FrameAnalyser : ${e.message}")
                    continue
                }
            }
            withContext(Dispatchers.Main) {
                // Clear the BoundingBoxOverlay and set the new results ( boxes ) to be displayed.
                boundingBoxOverlay.faceBoundingBoxes = predictions
                boundingBoxOverlay.invalidate()
                isProcessing = false
            }
        }
    }


    // Compute the L2 norm of ( x2 - x1 )
    private fun L2Norm(x1: FloatArray, x2: FloatArray): Float {
        return sqrt(x1.mapIndexed { i, xi -> (xi - x2[i]).pow(2) }.sum())
    }

    val listStudentDetect: MutableSet<String> = mutableSetOf()

    fun getListIdStudentDetect(): List<Int> {
        val set = listStudentDetect.toSet().toList()
        val listId = mutableListOf<Int>()
        for (item in set) {
            val nameArray = item.split("_")
            listId.add(nameArray.last().toInt())
        }
        return listId.toList()
    }


    // Compute the cosine of the angle between x1 and x2.
    private fun cosineSimilarity(x1: FloatArray, x2: FloatArray): Float {
        val mag1 = sqrt(x1.map { it * it }.sum())
        val mag2 = sqrt(x2.map { it * it }.sum())
        val dot = x1.mapIndexed { i, xi -> xi * x2[i] }.sum()
        return dot / (mag1 * mag2)
    }

}