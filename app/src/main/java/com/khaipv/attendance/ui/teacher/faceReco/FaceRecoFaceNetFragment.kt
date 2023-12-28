package com.khaipv.attendance.ui.teacher.faceReco

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Size
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.ActivityFaceRecoBinding
import com.khaipv.attendance.model.faceReco.FaceNetModel
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.testFaceReco.FileReader
import com.khaipv.attendance.testFaceReco.FrameAnalyser
import com.khaipv.attendance.testFaceReco.Logger
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject

//adb reverse tcp:3000 tcp:3000
@AndroidEntryPoint
class FaceRecoFaceNetFragment :
    BaseFragment<ActivityFaceRecoBinding, FaceRecoViewModel>(R.layout.activity_face_reco) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private lateinit var frameAnalyser: FrameAnalyser

    //    private lateinit var faceNetModel: FaceNetModel
    private lateinit var fileReader: FileReader
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    @Inject
    lateinit var faceNetModel: FaceNetModel

    private var cameraFacing = CameraSelector.LENS_FACING_BACK

    companion object {

        lateinit var logTextView: TextView

        fun setMessage(message: String) {
            logTextView.text = message
        }

    }

    private val viewModel: FaceRecoViewModel by viewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()

    private var courseId: Int? = null
    private var scheduleId: Int? = null

    override fun getVM(): FaceRecoViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.isLoading.postValue(true)
        logTextView = binding.logTextview
        logTextView.movementMethod = ScrollingMovementMethod()
        setUpBoundingBoxOverlay()
    }

    private fun setUpBoundingBoxOverlay() {
        val boundingBoxOverlay = binding.bboxOverlay
        boundingBoxOverlay.cameraFacing = cameraFacing

        boundingBoxOverlay.setWillNotDraw(false)
        boundingBoxOverlay.setZOrderOnTop(true)
        lifecycleScope.launch(Dispatchers.IO) {
            frameAnalyser = FrameAnalyser(requireContext(), boundingBoxOverlay, faceNetModel)
            fileReader = FileReader(faceNetModel)
            courseId = arguments?.getInt(BundleKey.COURSE_ID_ATTENDANCE)
            scheduleId = arguments?.getInt(BundleKey.SCHEDULE_ID_ATTENDANCE)
            if (courseId == null || scheduleId == null) {
                toastMessage("Error, try again")
                appNavigation.navigateUp()
            } else {
                viewModel.getAllImageFromCoursePerCycle(courseId!!, 0)
            }
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
            val bundle = Bundle()
            bundle.putInt(BundleKey.SCHEDULE_ID_ATTENDANCE, scheduleId!!)
            appNavigation.openFaceRecoFaceNetToListFaceReco(bundle)
        }

        binding.ivFlipCamera.setOnSafeClickListener {
            switchToCameraFront()
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.imagesData.collectFlowOnView(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is GetImageUiState.Success -> {
                        viewModel.isLoading.postValue(false)
                        if (uiState.image.size > 0) {
                            withContext(Dispatchers.Main) {
                                if (ActivityCompat.checkSelfPermission(
                                        requireContext(),
                                        Manifest.permission.CAMERA
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                } else {
                                    startCameraPreview()
                                }
                                if (uiState.image.size > 0) {
                                    frameAnalyser.faceList = uiState.image
                                }
                            }
                        } else {
                            toastMessage("Empty face data")
                            appNavigation.navigateUp()
                        }
                    }

                    is GetImageUiState.Error -> {
                        viewModel.isLoading.postValue(false)
                        toastMessage("Something went wrong, try again")
                        appNavigation.navigateUp()
                    }

                    else -> {}
                }

            }
        }

        lifecycleScope.launch {
            viewModel.listStudentRecognized.collectFlowOnView(viewLifecycleOwner) {
                shareViewModel.listStudentRecognized.value = it
            }
        }
    }

    private lateinit var cameraProvider: ProcessCameraProvider

    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
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

    private fun bindPreview(cameraProvider: ProcessCameraProvider, isBack: Boolean = true) {
        try {
            cameraProvider.unbindAll()
            val preview: Preview = Preview.Builder().build()
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraFacing)
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

    private fun switchToCameraFront() {
        cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        bindPreview(cameraProvider)
    }

    override fun onStop() {
        super.onStop()
        stopCamera()
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
