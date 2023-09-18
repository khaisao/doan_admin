package com.example.baseproject.ui.admin

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAdminHomeBinding
import com.example.core.base.fragment.BaseFragment

class AdminHomeFragment :
    BaseFragment<FragmentAdminHomeBinding, AdminHomeViewModel>(R.layout.fragment_admin_home) {

    private lateinit var navController: NavController

    private val viewModel: AdminHomeViewModel by viewModels()
    override fun getVM(): AdminHomeViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()

    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        navController =
            Navigation.findNavController(activity as Activity, R.id.top_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

    }
}