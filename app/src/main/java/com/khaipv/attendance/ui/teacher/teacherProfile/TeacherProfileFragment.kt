package com.khaipv.attendance.ui.teacher.teacherProfile

import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentTeacherProfileBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherProfileFragment :
    BaseFragment<FragmentTeacherProfileBinding, TeacherProfileViewModel>(R.layout.fragment_teacher_profile),
    PickiTCallbacks {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: TeacherProfileViewModel by viewModels()

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun getVM(): TeacherProfileViewModel = viewModel

    private var pickiT: PickiT? = null

    private val pickMediaForProfile =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                pickiT?.getPath(uri, Build.VERSION.SDK_INT)
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

        pickiT = PickiT(requireContext(), this, requireActivity())

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

        lifecycleScope.launch {
            viewModel.uploadAvatarActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it) {
                    toastMessage("Success")
                    viewModel.getTeacherInfo()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getTeacherInfoActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it) {
                    Glide.with(requireContext())
                        .load(rxPreferences.getAvatar())
                        .placeholder(R.drawable.no_avatar)
                        .into(binding.ivAvatar)
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
            appNavigation.openTeacherProfileToChangePassword()
        }

        binding.ivAvatar.setOnSafeClickListener {
            pickMediaForProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun PickiTonUriReturned() {
    }

    override fun PickiTonStartListener() {
    }

    override fun PickiTonProgressUpdate(progress: Int) {
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        val file = File(path)
        viewModel.updateTeacherAvatar(file)
    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }

}