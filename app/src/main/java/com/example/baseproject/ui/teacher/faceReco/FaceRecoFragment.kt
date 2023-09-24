package com.example.baseproject.ui.teacher.faceReco

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.Size
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityFaceRecoBinding
import com.example.baseproject.model.faceReco.FaceNetModel
import com.example.baseproject.model.faceReco.Models
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.shareData.ShareViewModel
import com.example.baseproject.testFaceReco.FileReader
import com.example.baseproject.testFaceReco.FrameAnalyser
import com.example.baseproject.testFaceReco.Logger
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.system.measureTimeMillis

//adb reverse tcp:3000 tcp:3000
@AndroidEntryPoint
class FaceRecoFragment :
    BaseFragment<ActivityFaceRecoBinding, FaceRecoViewModel>(R.layout.activity_face_reco) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private lateinit var frameAnalyser: FrameAnalyser

    //    private lateinit var faceNetModel: FaceNetModel
    private lateinit var fileReader: FileReader
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val useGpu = true

    private val useXNNPack = true

    private val modelInfo = Models.FACENET

    @Inject
    lateinit var faceNetModel: FaceNetModel

    private val cameraFacingBack = CameraSelector.LENS_FACING_BACK
    private val cameraFacingFront = CameraSelector.LENS_FACING_FRONT

    companion object {

        lateinit var logTextView: TextView

        fun setMessage(message: String) {
            logTextView.text = message
        }

    }

    private val viewModel: FaceRecoViewModel by viewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()

    override fun getVM(): FaceRecoViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.isLoading.postValue(true)
        logTextView = binding.logTextview
        logTextView.movementMethod = ScrollingMovementMethod()
        setUpBoundingBoxOverlay(true)
    }

    private fun setUpBoundingBoxOverlay(isLensBack: Boolean = true) {
        val boundingBoxOverlay = binding.bboxOverlay
        if (isLensBack) {
            boundingBoxOverlay.cameraFacing = cameraFacingBack
        } else {
            boundingBoxOverlay.cameraFacing = cameraFacingFront
        }
        boundingBoxOverlay.setWillNotDraw(false)
        boundingBoxOverlay.setZOrderOnTop(true)
        lifecycleScope.launch(Dispatchers.IO) {
            frameAnalyser = FrameAnalyser(requireContext(), boundingBoxOverlay, faceNetModel)
            fileReader = FileReader(faceNetModel)
            viewModel.getAllImageFromCoursePerCycle(1)
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvContinue.setOnSafeClickListener {
            val listStudentRecognized = shareViewModel.listStudentRecognized.value
            shareViewModel.listStudentRecognizedId.addAll(frameAnalyser.getListIdStudentDetect())
            listStudentRecognized.forEach { item ->
                if (shareViewModel.listStudentRecognizedId.contains(item.studentId)) {
                    item.isReco = true
                }
            }
            appNavigation.openFaceRecoToListFaceReco()
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.imagesData.collectFlowOnView(viewLifecycleOwner) {
                if (it.size > 0) {
                    withContext(Dispatchers.Main) {
                        if (ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestCameraPermission()
                        } else {
                            startCameraPreview()
                        }
                        if (it.size > 0) {
                            fileReader.run(it, fileReaderCallback)
                        }
                        viewModel.isLoading.postValue(false)
                    }
                }

            }
        }

        lifecycleScope.launch {
            viewModel.listStudentRecognized.collectFlowOnView(viewLifecycleOwner) {
                shareViewModel.listStudentRecognized.value = it
            }
        }
    }

    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun stopCamera() {
        if (this::cameraProviderFuture.isInitialized) {
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        try {
            cameraProvider.unbindAll()
            val preview: Preview = Preview.Builder().build()
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraFacingBack)
                .build()
            preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            val imageFrameAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(480, 640))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
            imageFrameAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), frameAnalyser)
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageFrameAnalysis
            )

        } catch (e: Exception) {

        }

    }

    override fun onStop() {
        super.onStop()
        stopCamera()
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraPreview()
            } else {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Camera Permission")
                    setMessage("The app couldn't function without the camera permission.")
                    setCancelable(false)
                    setPositiveButton("ALLOW") { dialog, which ->
                        dialog.dismiss()
                        requestCameraPermission()
                    }
                    setNegativeButton("CLOSE") { dialog, which ->
                        dialog.dismiss()
                        appNavigation.navigateUp()
                    }
                    create()
                }
                alertDialog.show()
            }

        }


    private val fileReaderCallback = object : FileReader.ProcessCallback {
        override fun onProcessCompleted(
            data: ArrayList<Pair<String, FloatArray>>,
            numImagesWithNoFaces: Int
        ) {
            frameAnalyser.faceList = data
            Logger.log("Images parsed. Found $numImagesWithNoFaces images with no faces.")
        }
    }


}
