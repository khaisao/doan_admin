package com.khaipv.attendance.ui.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentSplashBinding
import com.khaipv.attendance.model.faceReco.FaceNetModel
import com.khaipv.attendance.navigation.AppNavigation
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

    @Inject
    lateinit var faceNetModel: FaceNetModel

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
                        appNavigation.openSplashToAdminTop()
                    }
                    if (rxPreferences.getRole() == 2) {
                        appNavigation.openSplashToTeacherTop()
                    }
                    if (rxPreferences.getRole() == 1) {
                        appNavigation.openSplashToStudentTop()
                    }
                }
                if(it is LoginSplashEvent.LoginError){
                    appNavigation.openSplashToLoginScreen()
                }
            }
        }
    }

}