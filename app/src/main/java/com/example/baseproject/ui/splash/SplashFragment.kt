package com.example.baseproject.ui.splash

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSplashBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var rxPreferences:RxPreferences

    override fun getVM() = viewModel

    override fun bindingAction() {
        super.bindingAction()

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.loginActionStateFlow.collectFlowOnView(viewLifecycleOwner){
                if(it is LoginSplashEvent.LoginSuccess){
                    if (rxPreferences.getRole() == 3) {
                        appNavigation.openLoginToAdminTop()
                    }
                    if (rxPreferences.getRole() == 2) {
                        appNavigation.openSplashToTeacherTop()
                    }
                }
                if(it is LoginSplashEvent.LoginError){
                    appNavigation.openSplashToLoginScreen()
                }
            }
        }
    }

}