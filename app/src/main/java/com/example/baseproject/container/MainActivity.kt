package com.example.baseproject.container

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseActivityNotRequireViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityNotRequireViewModel<ActivityMainBinding>() {

    @Inject
    lateinit var appNavigation: AppNavigation

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)
    }
}