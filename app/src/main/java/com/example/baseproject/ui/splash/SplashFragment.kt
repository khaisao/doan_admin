package com.example.baseproject.ui.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSplashBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.setTextCompute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: SplashViewModel by viewModels()

    override fun getVM() = viewModel

    override fun bindingAction() {
        super.bindingAction()

        lifecycleScope.launch {
            delay(100)
            appNavigation.openSplashToLoginScreen()
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
    }

}