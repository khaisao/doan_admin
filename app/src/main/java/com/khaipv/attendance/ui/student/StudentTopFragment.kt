package com.khaipv.attendance.ui.student

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentStudentTopBinding
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.util.cameraPermissionsGranted
import com.khaipv.attendance.util.openAppSystemSettings
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch


class StudentTopFragment :
    BaseFragment<FragmentStudentTopBinding, StudentTopViewModel>(R.layout.fragment_student_top) {

    private lateinit var navController: NavController

    private val viewModel: StudentTopViewModel by viewModels()
    override fun getVM(): StudentTopViewModel = viewModel

    private val shareViewModel: ShareViewModel by activityViewModels()

    private lateinit var alertDialog: AlertDialog

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()
        alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Camera Permission")
            setMessage("The app couldn't function without the camera permission.")
            setCancelable(false)
            setPositiveButton("ALLOW") { dialog, which ->
                requireContext().openAppSystemSettings()
            }
        }.create()
        PermissionX.init(requireActivity())
            .permissions(Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                alertDialog.show()
            }
            .onForwardToSettings { scope, deniedList ->
                alertDialog.show()
            }
            .request { allGranted, grantedList, deniedList ->

            }
    }

    override fun onResume() {
        super.onResume()
        if (!requireActivity().cameraPermissionsGranted()) {
            if (!alertDialog.isShowing) {
                alertDialog.show()
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            shareViewModel.positionBottomNavFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it == 1) {
                    binding.bottomNavStudent.selectedItemId = R.id.studentProfileFragment
                }
            }
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNavStudent
        navController =
            Navigation.findNavController(activity as Activity, R.id.top_student_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

    }
}