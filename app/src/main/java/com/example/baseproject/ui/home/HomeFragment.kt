package com.example.baseproject.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentHomeBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: HomeViewModel by viewModels()

    override val layoutId = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun getVM(): HomeViewModel = viewModel

}