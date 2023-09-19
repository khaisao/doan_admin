package com.example.baseproject.ui.changePassword

import android.os.Bundle
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentChangePasswordSuccessBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
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