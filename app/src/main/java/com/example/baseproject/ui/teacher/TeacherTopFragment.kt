package com.example.baseproject.ui.teacher

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentTeacherTopBinding
import com.example.baseproject.shareData.ShareViewModel
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import kotlinx.coroutines.launch

class TeacherTopFragment :
    BaseFragment<FragmentTeacherTopBinding, TeacherTopViewModel>(R.layout.fragment_teacher_top) {

    private lateinit var navController: NavController

    private val viewModel: TeacherTopViewModel by viewModels()
    override fun getVM(): TeacherTopViewModel = viewModel

    private val shareViewModel: ShareViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()

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
            shareViewModel.positionBottomNavFlow.collectFlowOnView(viewLifecycleOwner){
                if(it == 1){
                    binding.bottomNav.selectedItemId = R.id.tabTeacherProfile
                }
            }
        }
    }
}