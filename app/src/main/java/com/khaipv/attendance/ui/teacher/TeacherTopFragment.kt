package com.khaipv.attendance.ui.teacher

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
import com.khaipv.attendance.databinding.FragmentTeacherTopBinding
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.util.cameraPermissionsGranted
import com.khaipv.attendance.util.openAppSystemSettings
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch

class TeacherTopFragment :
    BaseFragment<FragmentTeacherTopBinding, TeacherTopViewModel>(R.layout.fragment_teacher_top) {

    private lateinit var navController: NavController

    private val viewModel: TeacherTopViewModel by viewModels()
    override fun getVM(): TeacherTopViewModel = viewModel

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
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (!allGranted) {
                    alertDialog.show()
                }
            }

    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        navController =
            Navigation.findNavController(activity as Activity, R.id.top_teacher_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            shareViewModel.positionBottomNavFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it == 1) {
                    binding.bottomNav.selectedItemId = R.id.tabTeacherProfile
                }
            }
        }
    }
}