
package com.example.baseproject.ui.teacher.faceReco

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityFaceRecoBinding
import com.example.baseproject.model.faceReco.FaceNetModel
import com.example.baseproject.model.faceReco.Models
import com.example.baseproject.testFaceReco.FileReader
import com.example.baseproject.testFaceReco.FrameAnalyser
import com.example.baseproject.testFaceReco.Logger
import com.example.core.base.fragment.BaseFragment
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import java.util.concurrent.Executors

@AndroidEntryPoint
class FaceRecoFragment :
    BaseFragment<ActivityFaceRecoBinding, FaceRecoViewModel>(R.layout.activity_face_reco) {

    private lateinit var previewView : PreviewView
    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var faceNetModel : FaceNetModel
    private lateinit var fileReader : FileReader
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private val useGpu = true

    private val useXNNPack = true

    private val modelInfo = Models.FACENET

    private val cameraFacing = CameraSelector.LENS_FACING_BACK

    companion object {

        lateinit var logTextView : TextView

        fun setMessage( message : String ) {
            logTextView.text = message
        }

    }
    private val viewModel: FaceRecoViewModel by viewModels()

    override fun getVM(): FaceRecoViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        previewView = binding.previewView
        logTextView = binding.logTextview
        logTextView.movementMethod = ScrollingMovementMethod()
        val boundingBoxOverlay = binding.bboxOverlay
        boundingBoxOverlay.cameraFacing = cameraFacing
        boundingBoxOverlay.setWillNotDraw( false )
        boundingBoxOverlay.setZOrderOnTop( true )

        lifecycleScope.launch(Dispatchers.IO) {
            faceNetModel = FaceNetModel( requireContext() , modelInfo , useGpu , useXNNPack )
            frameAnalyser = FrameAnalyser( requireContext() , boundingBoxOverlay , faceNetModel )
            fileReader = FileReader( faceNetModel )
            withContext(Dispatchers.Main){
                if ( ActivityCompat.checkSelfPermission( requireContext() , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    requestCameraPermission()
                }
                else {
                    startCameraPreview()
                }

                launchChooseDirectoryIntent()
            }

        }
    }

    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance( requireContext() )
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider) },
            ContextCompat.getMainExecutor(requireContext()) )
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
            val alertDialog = AlertDialog.Builder( requireContext() ).apply {
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

    private val fileReaderCallback = object : FileReader.ProcessCallback {
        override fun onProcessCompleted(data: ArrayList<Pair<String, FloatArray>>, numImagesWithNoFaces: Int) {
            frameAnalyser.faceList = data
            Logger.log("Images parsed. Found $numImagesWithNoFaces images with no faces.")
        }
    }


}
