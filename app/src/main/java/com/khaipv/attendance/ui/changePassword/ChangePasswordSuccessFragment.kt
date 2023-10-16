package com.khaipv.attendance.ui.changePassword

import android.os.Bundle
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentChangePasswordSuccessBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragmentNotRequireViewModel
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordSuccessFragment :
    BaseFragmentNotRequireViewModel<FragmentChangePasswordSuccessBinding>(R.layout.fragment_change_password_success) {

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val title = arguments?.getString(BundleKey.TITLE_ACTION_SUCCESS)
        val des = arguments?.getString(BundleKey.DES_ACTION_SUCCESS)
        binding.tvTitle.text = title
        binding.tvDes.text = des
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvBack.setOnSafeClickListener {
            appNavigation.navigateUp()
        }
    }
}