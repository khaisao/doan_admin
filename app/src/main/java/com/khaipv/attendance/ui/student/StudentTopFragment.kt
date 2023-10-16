package com.khaipv.attendance.ui.student

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentStudentTopBinding
import com.khaipv.attendance.shareData.ShareViewModel
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.toastMessage
import kotlinx.coroutines.launch

class StudentTopFragment :
    BaseFragment<FragmentStudentTopBinding, StudentTopViewModel>(R.layout.fragment_student_top) {

    private lateinit var navController: NavController

    private val viewModel: StudentTopViewModel by viewModels()
    override fun getVM(): StudentTopViewModel = viewModel

    private val shareViewModel: ShareViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            shareViewModel.positionBottomNavFlow.collectFlowOnView(viewLifecycleOwner){
                if(it == 1){
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