package com.example.baseproject.ui.admin.adminProfile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAdminProfileBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AdminProfileFragment :
    BaseFragment<FragmentAdminProfileBinding, AdminProfileViewModel>(R.layout.fragment_admin_profile) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: AdminProfileViewModel by viewModels()

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun getVM(): AdminProfileViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        Glide.with(requireContext())
            .load(R.drawable.no_avatar)
            .transform(CenterInside(), RoundedCorners(100))
            .into(binding.ivAvatar)

        binding.tvName.text = rxPreferences.getName()
        binding.tvUsername.text = rxPreferences.getUserName()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.loginActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is LogoutEvent.LogoutSuccess) {
                    appNavigation.openLoginScreenAndClearBackStack()
                }
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvLogOut.setOnClickListener {
            viewModel.logOut()
        }

        binding.clChangePassword.setOnSafeClickListener {
            appNavigation.openProfileToChangePassword()
        }
    }

}