package com.example.baseproject.ui.admin.adminProfile

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAdminHomeBinding
import com.example.core.base.fragment.BaseFragment

class AdminProfileFragment :
    BaseFragment<FragmentAdminHomeBinding, AdminProfileViewModel>(R.layout.fragment_admin_profile) {
    private val viewModel: AdminProfileViewModel by viewModels()

    override fun getVM(): AdminProfileViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

    }

}