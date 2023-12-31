package com.khaipv.attendance.ui.login

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentLoginBinding
import com.khaipv.attendance.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var rxPreferences: RxPreferences

    private val viewModel: LoginViewModel by viewModels()

    private var fcmDeviceToken: String? = null

    override fun getVM(): LoginViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            fcmDeviceToken = it
        }
        binding.edtUser.doOnTextChanged { text, start, before, count ->
            binding.tvErrorUsername.isVisible = false
        }
        binding.edtPassword.doOnTextChanged { text, start, before, count ->
            binding.tvErrorPassword.isVisible = false
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.ivShowIcon.setOnSafeClickListener {
            Log.d("asgawgawgagw", "setOnClick: ${binding.edtPassword.inputType}")
            if (binding.edtPassword.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT
                binding.ivShowIcon.loadImage(R.drawable.ic_visible_password)
            } else {
                binding.ivShowIcon.loadImage(R.drawable.ic_invisible_password)
                binding.edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        binding.tvLogin.setOnSafeClickListener {
            val user = binding.edtUser.text.toString()
            val password = binding.edtPassword.text.toString()
            if (isValidLogin(user, password) && fcmDeviceToken != null) {
                viewModel.login(user, password, fcmDeviceToken!!)
            }
        }

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.loginActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is LoginEvent.LoginSuccess) {
                    if (rxPreferences.getRole() == 3) {
                        appNavigation.openLoginToAdminTop()
                    }
                    if (rxPreferences.getRole() == 2) {
                        appNavigation.openLoginToTeacherTop()
                    }
                    if (rxPreferences.getRole() == 1) {
                        appNavigation.openLoginToStudentTop()
                    }
                }
            }
        }
    }

    private fun isValidLogin(user: String, password: String): Boolean {
        var isValid = true
        if (user.isEmpty()) {
            binding.tvErrorUsername.text = getString(R.string.username_is_empty)
            binding.tvErrorUsername.isVisible = true
            isValid = false
        } else if (user.isNotEmpty()) {
            if (user.length < 6) {
                binding.tvErrorUsername.text = getString(R.string.username_is_invalid)
                binding.tvErrorUsername.isVisible = true
                isValid = false
            }
        }

        if (password.isEmpty()) {
            binding.tvErrorPassword.text = getString(R.string.password_is_empty)
            binding.tvErrorPassword.isVisible = true
            isValid = false
        } else if (password.isNotEmpty()) {
            if (password.length < 6) {
                binding.tvErrorPassword.text = getString(R.string.password_is_invalid)
                binding.tvErrorPassword.isVisible = true
                isValid = false
            }
        }
        return isValid
    }

}