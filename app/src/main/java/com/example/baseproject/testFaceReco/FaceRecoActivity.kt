/*
 * Copyright 2023 Shubham Panchal
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.baseproject.testFaceReco

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.Size
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.databinding.ActivityFaceRecoBinding
import com.example.baseproject.model.faceReco.FaceNetModel
import com.example.baseproject.model.faceReco.Models
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import java.util.concurrent.Executors

class FaceRecoActivity : AppCompatActivity() {

    private var isSerializedDataStored = false

    // Serialized data will be stored ( in app's private storage ) with this filename.
    private val SERIALIZED_DATA_FILENAME = "image_data"

    // Shared Pref key to check if the data was stored.
    private val SHARED_PREF_IS_DATA_STORED_KEY = "is_data_stored"

    private lateinit var activityMainBinding : ActivityFaceRecoBinding
    private lateinit var previewView : PreviewView
    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var faceNetModel : FaceNetModel
    private lateinit var fileReader : FileReader
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>



    // <----------------------- User controls --------------------------->

    // Use the device's GPU to perform faster computations.
    // Refer https://www.tensorflow.org/lite/performance/gpu
    private val useGpu = true

    // Use XNNPack to accelerate inference.
    // Refer https://blog.tensorflow.org/2020/07/accelerating-tensorflow-lite-xnnpack-integration.html
    private val useXNNPack = true

    // You may the change the models here.
    // Use the model configs in Models.kt
    // Default is Models.FACENET ; Quantized models are faster
    private val modelInfo = Models.FACENET

    // Camera Facing
    private val cameraFacing = CameraSelector.LENS_FACING_BACK

    // <---------------------------------------------------------------->


    companion object {

        lateinit var logTextView : TextView

        fun setMessage( message : String ) {
            logTextView.text = message
        }

    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove the status bar to have a full screen experience
        // See this answer on SO -> https://stackoverflow.com/a/68152688/10878733
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.decorView.windowInsetsController!!
//                .hide( WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//        }
//        else {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
        activityMainBinding = ActivityFaceRecoBinding.inflate( layoutInflater )
        setContentView( activityMainBinding.root )

        previewView = activityMainBinding.previewView
        logTextView = activityMainBinding.logTextview
        logTextView.movementMethod = ScrollingMovementMethod()
        // Necessary to keep the Overlay above the PreviewView so that the boxes are visible.
        val boundingBoxOverlay = activityMainBinding.bboxOverlay
        boundingBoxOverlay.cameraFacing = cameraFacing
        boundingBoxOverlay.setWillNotDraw( false )
        boundingBoxOverlay.setZOrderOnTop( true )

        faceNetModel = FaceNetModel( this , modelInfo , useGpu , useXNNPack )
        frameAnalyser = FrameAnalyser( this , boundingBoxOverlay , faceNetModel )
        fileReader = FileReader( faceNetModel )


        // We'll only require the CAMERA permission from the user.
        // For scoped storage, particularly for accessing documents, we won't require WRITE_EXTERNAL_STORAGE or
        // READ_EXTERNAL_STORAGE permissions. See https://developer.android.com/training/data-storage
        if ( ActivityCompat.checkSelfPermission( this , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            requestCameraPermission()
        }
        else {
            startCameraPreview()
        }

        launchChooseDirectoryIntent()

    }

    // ---------------------------------------------- //

    // Attach the camera stream to the PreviewView.
    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance( this )
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider) },
            ContextCompat.getMainExecutor(this) )
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder().build()
        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing( cameraFacing )
            .build()
        preview.setSurfaceProvider( previewView.surfaceProvider )
        val imageFrameAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size( 480, 640 ) )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
        imageFrameAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), frameAnalyser )
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview , imageFrameAnalysis  )
    }

    // We let the system handle the requestCode. This doesn't require onRequestPermissionsResult and
    // hence makes the code cleaner.
    // See the official docs -> https://developer.android.com/training/permissions/requesting#request-permission
    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch( Manifest.permission.CAMERA )
    }

    private val cameraPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() ) {
        isGranted ->
        if ( isGranted ) {
            startCameraPreview()
        }
        else {
            val alertDialog = AlertDialog.Builder( this ).apply {
                setTitle( "Camera Permission")
                setMessage( "The app couldn't function without the camera permission." )
                setCancelable( false )
                setPositiveButton( "ALLOW" ) { dialog, which ->
                    dialog.dismiss()
                    requestCameraPermission()
                }
                setNegativeButton( "CLOSE" ) { dialog, which ->
                    dialog.dismiss()
//                    appNavigation.navigateUp()
                }
                create()
            }
            alertDialog.show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun launchChooseDirectoryIntent() {
//        val intent = Intent( Intent.ACTION_OPEN_DOCUMENT_TREE )
//        // startForActivityResult is deprecated.
//        // See this SO thread -> https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
//        directoryAccessLauncher.launch( intent )
        lifecycleScope.launch {
            val images = ArrayList<Pair<String, Bitmap>>()
            val bitmap1 =
                getBitmapFromUrl("https://vtv1.mediacdn.vn/zoom/640_400/2022/12/23/untitled-1-1671764003830832199181-crop-1671764010508138109731.jpg")
            if (bitmap1 != null) {
                images.add(Pair("JustinBieBer", bitmap1))
            } else {
                Log.d("asgasgasg", "null r: ")
            }
            val bitmap2 =
                getBitmapFromUrl("https://cdn.tuoitre.vn/thumb_w/640/2020/6/22/justin-bieber-1-1592797878809968908887.jpeg")
            if (bitmap2 != null) {
                images.add(Pair("JustinBieBer", bitmap2))
            } else {
                Log.d("asgasgasg", "null r2: ")
            }
            val bitmap3 =
                getBitmapFromUrl("https://cdn.tgdd.vn/Files/2022/02/21/1416573/bill-gates_1280x720-800-resize.jpg")
            if (bitmap3 != null) {
                images.add(Pair("BillGate", bitmap3))
            } else {
                Log.d("asgasgasg", "null r2: ")
            }
            val bitmap4 =
                getBitmapFromUrl("https://cdn.britannica.com/47/188747-050-1D34E743/Bill-Gates-2011.jpg")
            if (bitmap4 != null) {
                images.add(Pair("BillGate", bitmap4))
            } else {
                Log.d("asgasgasg", "null r2: ")
            }
            withContext(Dispatchers.Main) {
                fileReader.run(images, fileReaderCallback)
            }
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


    // ---------------------------------------------- //


    private val fileReaderCallback = object : FileReader.ProcessCallback {
        override fun onProcessCompleted(data: ArrayList<Pair<String, FloatArray>>, numImagesWithNoFaces: Int) {
            frameAnalyser.faceList = data
//            Logger.log( "Images parsed. Found $numImagesWithNoFaces images with no faces." )
        }
    }


}
