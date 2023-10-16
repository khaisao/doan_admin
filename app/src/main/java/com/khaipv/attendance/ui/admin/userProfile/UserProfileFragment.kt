package com.khaipv.attendance.ui.admin.userProfile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentUserProfileBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.ui.admin.userProfile.adapter.UserProfileImageViewAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment :
    BaseFragment<FragmentUserProfileBinding, UserProfileViewModel>(R.layout.fragment_user_profile) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: UserProfileViewModel by viewModels()

    override fun getVM(): UserProfileViewModel = viewModel

    private lateinit var adapter: UserProfileImageViewAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        Glide.with(requireContext())
            .load(R.drawable.no_avatar)
            .transform(CenterInside(), RoundedCorners(100))
            .into(binding.ivAvatar)
        adapter = UserProfileImageViewAdapter()
        binding.rvImageProfile.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvImageProfile.adapter = adapter
        val listUrl = listOf(
            "https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000",
            "https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000",
            "https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000",
            "https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000"
        )
        adapter.submitList(listUrl)
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
    }

}