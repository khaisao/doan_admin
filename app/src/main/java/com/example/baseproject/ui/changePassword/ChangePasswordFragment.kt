package com.example.baseproject.ui.changePassword

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentChangePasswordBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>(R.layout.fragment_change_password) {

    @Inject
    lateinit var rxPreferences: RxPreferences

    private val viewModel: ChangePasswordViewModel by viewModels()

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun getVM(): ChangePasswordViewModel = viewModel

    override fun setOnClick() {
        super.setOnClick()
        binding.ivViewCurrentPassword.setOnSafeClickListener {
            if (binding.edtCurrentPassword.inputType == 129) {
                binding.edtCurrentPassword.inputType = InputType.TYPE_CLASS_TEXT
                binding.ivViewCurrentPassword.loadImage(R.drawable.ic_visible_password)
            } else {
                binding.ivViewCurrentPassword.loadImage(R.drawable.ic_invisible_password)
                binding.edtCurrentPassword.inputType = 129
            }
        }

        binding.ivViewNewPassword.setOnSafeClickListener {
            if (binding.edtNewPassword.inputType == 129) {
                binding.edtNewPassword.inputType = InputType.TYPE_CLASS_TEXT
                binding.ivViewNewPassword.loadImage(R.drawable.ic_visible_password)
            } else {
                binding.ivViewNewPassword.loadImage(R.drawable.ic_invisible_password)
                binding.edtNewPassword.inputType = 129
            }
        }

        binding.ivViewConfirmPassword.setOnSafeClickListener {
            if (binding.edtConfirmPassword.inputType == 129) {
                binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT
                binding.ivViewConfirmPassword.loadImage(R.drawable.ic_visible_password)
            } else {
                binding.ivViewConfirmPassword.loadImage(R.drawable.ic_invisible_password)
                binding.edtConfirmPassword.inputType = 129
            }
        }

        binding.tvConfirm.setOnSafeClickListener {
            val currentPassword = binding.edtCurrentPassword.text.toString()
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            if (isValidChangePassword(currentPassword, newPassword, confirmPassword)) {
                viewModel.changePassword(newPassword)
            }
        }

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.changePasswordActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                val bundle = Bundle()
                bundle.putString(BundleKey.TITLE_ACTION_SUCCESS, "Change password success")
                bundle.putString(
                    BundleKey.DES_ACTION_SUCCESS,
                    "Congratulation! Change password success. Please continue your work"
                )
                if (it is ChangePasswordEvent.ChangePasswordSuccess) {
                    if (rxPreferences.getRole() == 1) {
                        appNavigation.openStudentToChangePasswordSuccess(bundle)
                    }
                    if (rxPreferences.getRole() == 2) {
                        appNavigation.openTeacherToChangePasswordSuccess(bundle)
                    }
                    if (rxPreferences.getRole() == 3) {
                        appNavigation.openAdminToChangePasswordSuccess(bundle)
                    }
                }
            }
        }
    }

    private fun isValidChangePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true
        val currentLoginPassword = rxPreferences.getPassword()
        if (currentPassword.isEmpty()) {
            binding.tvErrorCurrentPassword.text = getString(R.string.current_password_is_empty)
            binding.tvErrorCurrentPassword.isVisible = true
            isValid = false
        } else {
            if (currentPassword.length < 5) {
                binding.tvErrorCurrentPassword.text = getString(R.string.password_length_required_5)
                binding.tvErrorCurrentPassword.isVisible = true
                isValid = false
            } else {
                if (currentPassword != currentLoginPassword) {
                    binding.tvErrorCurrentPassword.text =
                        getString(R.string.current_password_incorrect)
                    binding.tvErrorCurrentPassword.isVisible = true
                    isValid = false
                }
            }
        }

        if (newPassword.isEmpty()) {
            binding.tvErrorNewPassword.text = getString(R.string.new_password_is_empty)
            binding.tvErrorNewPassword.isVisible = true
            isValid = false
        } else {
            if (newPassword.length < 6) {
                binding.tvErrorNewPassword.text = getString(R.string.new_password_length_required_5)
                binding.tvErrorNewPassword.isVisible = true
                isValid = false
            }
        }

        if (confirmPassword != newPassword) {
            binding.tvErrorConfirmPassword.text = getString(R.string.password_not_match)
            binding.tvErrorConfirmPassword.isVisible = true
            isValid = false
        }

        return isValid
    }

}