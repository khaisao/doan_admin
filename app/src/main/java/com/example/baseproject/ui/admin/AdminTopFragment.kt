package com.example.baseproject.ui.admin

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAdminTopBinding
import com.example.core.base.fragment.BaseFragment

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