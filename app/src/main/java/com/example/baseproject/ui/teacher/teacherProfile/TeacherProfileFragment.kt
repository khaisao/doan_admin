package com.example.baseproject.ui.teacher.teacherProfile

import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAdminProfileBinding
import com.example.baseproject.databinding.FragmentTeacherProfileBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TeacherProfileFragment :
    BaseFragment<FragmentTeacherProfileBinding, TeacherProfileViewModel>(R.layout.fragment_teacher_profile) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: TeacherProfileViewModel by viewModels()

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun getVM(): TeacherProfileViewModel = viewModel

    private val pickMediaForProfile =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {

            } else {
            }
        }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        Glide.with(requireContext())
            .load(rxPreferences.getAvatar())
            .placeholder(R.drawable.no_avatar)
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
        binding.tvLogOut.setOnSafeClickListener {
            viewModel.logOut()
        }

        binding.clChangePassword.setOnSafeClickListener {
        }

        binding.ivAvatar.setOnSafeClickListener {
            pickMediaForProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}