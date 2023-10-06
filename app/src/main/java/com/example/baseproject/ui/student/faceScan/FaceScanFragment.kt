package com.example.baseproject.ui.student.faceScan

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFaceScanBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.student.faceScan.camerax.CameraManager
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FaceScanFragment :
    BaseFragment<FragmentFaceScanBinding, FaceScanViewModel>(R.layout.fragment_face_scan) {
    private val viewModel: FaceScanViewModel by viewModels()

    override fun getVM(): FaceScanViewModel = viewModel


    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        createCameraManager()
        checkForPermission()
        onClicks()
    }

    override fun bindingStateView() {
        super.bindingStateView()

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
            onSuccessImageRight = {
                binding.ivArrowRight.isVisible = false
                binding.ivArrowBottom.isVisible = true
            },
            onSuccessImageTop = {
                binding.ivArrowTop.isVisible = false
                binding.ivArrowRight.isVisible = true
            },
            onSuccessImageBottom = {
                binding.ivArrowBottom.isVisible = false
                binding.ivArrowLeft.isVisible = true
            },
            onSuccessImageLeft = {
                binding.ivArrowLeft.isVisible = false

            },
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

}