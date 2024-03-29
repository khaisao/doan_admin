package com.khaipv.attendance.ui.admin

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentAdminTopBinding
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminTopFragment :
    BaseFragment<FragmentAdminTopBinding, AdminTopViewModel>(R.layout.fragment_admin_top) {

    private lateinit var navController: NavController

    private val viewModel: AdminTopViewModel by viewModels()
    override fun getVM(): AdminTopViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        navController =
            Navigation.findNavController(activity as Activity, R.id.top_admin_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }
}