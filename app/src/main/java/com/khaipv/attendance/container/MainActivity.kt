package com.khaipv.attendance.container

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.core.base.activity.BaseActivityNotRequireViewModel
import com.example.core.pref.RxPreferences
import com.example.core.utils.NetworkConnectionManager
import com.example.core.utils.setLanguage
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.ActivityMainBinding
import com.khaipv.attendance.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivityNotRequireViewModel<ActivityMainBinding>() {

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    @Inject
    lateinit var rxPreferences: RxPreferences

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)

        lifecycleScope.launch {
            val language = rxPreferences.getLanguage().first()
            language?.let { setLanguage(it) }
        }

        networkConnectionManager.isNetworkConnectedFlow
            .onEach {
                if (it) {
                    Timber.tag("ahihi").d("onCreate: Network connected")
                } else {
                    Timber.tag("ahihi").d("onCreate: Network disconnected")
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        networkConnectionManager.startListenNetworkState()
    }

    override fun onStop() {
        super.onStop()
        networkConnectionManager.stopListenNetworkState()
    }

}