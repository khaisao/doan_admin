package com.example.baseproject.ui.login

import android.os.Bundle
import android.text.InputType
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.isValidEmailInput
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: LoginViewModel by viewModels()

    override fun getVM(): LoginViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.edtUser.doOnTextChanged { text, start, before, count ->
            binding.tvErrorEmail.isVisible = false
        }
        binding.edtPassword.doOnTextChanged { text, start, before, count ->
            binding.tvErrorPassword.isVisible = false
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.ivShowIcon.setOnSafeClickListener {
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
            if (isValidLogin(user, password)) {
                viewModel.login(user, password)
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.loginActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is LoginEvent.LoginSuccess) {
                    appNavigation.openLoginToAdminHome()
                }
            }
        }
    }

    private fun isValidLogin(user: String, password: String): Boolean {
        var isValid = true
        if (user.isEmpty()) {
            binding.tvErrorEmail.text = getString(R.string.email_is_empty)
            binding.tvErrorEmail.isVisible = true
            isValid = false
        } else if (user.isNotEmpty()) {
            if (!user.isValidEmailInput()) {
                binding.tvErrorEmail.text = getString(R.string.email_is_invalid)
                binding.tvErrorEmail.isVisible = true
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