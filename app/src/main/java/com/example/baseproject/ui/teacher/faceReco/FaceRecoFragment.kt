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
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.shareData.ShareViewModel
import com.example.baseproject.testFaceReco.FileReader
import com.example.baseproject.testFaceReco.FrameAnalyser
import com.example.baseproject.testFaceReco.Logger
import com.example.baseproject.util.BundleKey
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
class FaceRecoFragment :
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
                viewModel.getAllImageFromCoursePerCycle(courseId!!)
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
            appNavigation.openFaceRecoToListFaceReco(bundle)
        }

        binding.ivFlipCamera.setOnSafeClickListener {
            switchToCameraFront()
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
//                        if (it.size > 0) {
//                            fileReader.run(it, fileReaderCallback)
//                        }
                        if(it.size > 0){
                            frameAnalyser.faceList = it
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
