package com.khaipv.attendance.ui.student.faceScan

import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentFaceScanBinding
import com.khaipv.attendance.model.faceReco.FaceNetModel
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.ui.student.faceScan.camerax.CameraManager
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.kbyai.facesdk.FaceBox
import com.kbyai.facesdk.FaceDetectionParam
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.util.BundleKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class FaceScanFragment :
    BaseFragment<FragmentFaceScanBinding, FaceScanViewModel>(R.layout.fragment_face_scan) {
    private val viewModel: FaceScanViewModel by viewModels()

    override fun getVM(): FaceScanViewModel = viewModel

    @Inject
    lateinit var faceNetModel: FaceNetModel

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    private lateinit var dialog: DialogLookTheCameraFragment

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        dialog = DialogLookTheCameraFragment()
        dialog.isCancelable = false
        dialog.show(childFragmentManager,DialogLookTheCameraFragment::class.java.simpleName)
        createCameraManager()
        checkForPermission()
        onClick()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.isScanSuccess.collectFlowOnView(viewLifecycleOwner) {
                if (it != null && it == true) {
                    val bundle = Bundle()
                    bundle.putString(BundleKey.TITLE_ACTION_SUCCESS, "Add face success")
                    bundle.putString(
                        BundleKey.DES_ACTION_SUCCESS,
                        "Congratulation! Add face success. Please continue your work"
                    )
                    appNavigation.openFaceScanToFaceScanSuccess(bundle)
                }
            }
        }
    }

    private fun onClick() {
        binding.ivFlipCamera.setOnClickListener {
            cameraManager.changeCameraSelector()
        }

    }

    private fun checkForPermission() {
        cameraManager.startCamera()
    }

    private lateinit var cameraManager: CameraManager

    private val listFaceNetDataImage: MutableList<String> = mutableListOf()
    private val listKbyDataImage: MutableList<String> = mutableListOf()

    private fun createCameraManager() {
        binding.ivArrowTop.isVisible = true
        binding.ivArrowRight.isVisible = false
        binding.ivArrowBottom.isVisible = false
        binding.ivArrowLeft.isVisible = false
        val faceDetectionParam = FaceDetectionParam()
        cameraManager = CameraManager(
            requireContext(),
            binding.previewViewFinder,
            viewLifecycleOwner,
            binding.graphicOverlayFinder,
            onSuccessImageFront = {
                listFaceNetDataImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                val faceBoxes: List<FaceBox>? = FaceSDK.faceDetection(it, faceDetectionParam)
//                val templates = FaceSDK.templateExtraction(bitmap, faceBoxes[0])
//                val byteHex = templates.toHex3()
//                listKbyDataImage.add()
                saveImageScan(it, "front")
                dialog.dismiss()
            },
            onSuccessImageRight = {
                binding.ivArrowRight.isVisible = false
                binding.ivArrowBottom.isVisible = true
                listFaceNetDataImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                saveImageScan(it, "right")
            },
            onSuccessImageTop = {
                listFaceNetDataImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                binding.ivArrowTop.isVisible = false
                binding.ivArrowRight.isVisible = true
                saveImageScan(it, "top")

            },
            onSuccessImageBottom = {
                listFaceNetDataImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                binding.ivArrowBottom.isVisible = false
                binding.ivArrowLeft.isVisible = true
                saveImageScan(it, "bottom")

            },
            onSuccessImageLeft = {
                listFaceNetDataImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                viewModel.addImageProfileFaceNetModel(listFaceNetDataImage)
                binding.ivArrowLeft.isVisible = false
                saveImageScan(it, "left")
            },
        )
    }

    private fun saveImageScan(bitmap: Bitmap, fileName: String) {
        val cacheDir = requireContext().cacheDir
        val file =
            File(cacheDir, "$fileName.jpg") // Thay đổi tên và định dạng tệp ảnh tùy ý

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                stream
            )
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getStringFromEmbed(data: FloatArray): String {
        val stringBuilder = StringBuilder()
        for (value in data) {
            stringBuilder.append(value)
                .append(",")
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.setLength(stringBuilder.length - 2)
        }
        return stringBuilder.toString()
    }

}