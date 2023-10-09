package com.example.baseproject.ui.student.faceScan

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFaceScanBinding
import com.example.baseproject.model.faceReco.FaceNetModel
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.student.faceScan.camerax.CameraManager
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.toastMessage
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
        onClicks()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.isScanSuccess.collectFlowOnView(viewLifecycleOwner) {
                if (it != null && it == true) {
                    toastMessage("Success, Please continue your work")
                    appNavigation.navigateUp()
                }
            }
        }
    }

    private fun onClicks() {
        binding.ivFlipCamera.setOnClickListener {
            cameraManager.changeCameraSelector()
        }

    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private lateinit var cameraManager: CameraManager

    private val listBitmapImage: MutableList<String> = mutableListOf()

    private fun createCameraManager() {
        binding.ivArrowTop.isVisible = true
        binding.ivArrowRight.isVisible = false
        binding.ivArrowBottom.isVisible = false
        binding.ivArrowLeft.isVisible = false

        cameraManager = CameraManager(
            requireContext(),
            binding.previewViewFinder,
            viewLifecycleOwner,
            binding.graphicOverlayFinder,
            onSuccessImageFront = {
                listBitmapImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                saveImageScan(it, "front")
                dialog.dismiss()
            },
            onSuccessImageRight = {
                binding.ivArrowRight.isVisible = false
                binding.ivArrowBottom.isVisible = true
                listBitmapImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                saveImageScan(it, "right")
            },
            onSuccessImageTop = {
                listBitmapImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                binding.ivArrowTop.isVisible = false
                binding.ivArrowRight.isVisible = true
                saveImageScan(it, "top")

            },
            onSuccessImageBottom = {
                listBitmapImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                binding.ivArrowBottom.isVisible = false
                binding.ivArrowLeft.isVisible = true
                saveImageScan(it, "bottom")

            },
            onSuccessImageLeft = {
                listBitmapImage.add(getStringFromEmbed(faceNetModel.getFaceEmbedding(it)))
                viewModel.addImageProfile(listBitmapImage)
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

}