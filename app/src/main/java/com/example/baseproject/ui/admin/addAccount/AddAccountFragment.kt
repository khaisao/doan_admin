package com.example.baseproject.ui.admin.addAccount

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAddAccountBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.baseproject.util.isValidEmailInput
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddAccountFragment :
    BaseFragment<FragmentAddAccountBinding, AddAccountViewModel>(R.layout.fragment_add_account) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: AddAccountViewModel by viewModels()
    override fun getVM(): AddAccountViewModel = viewModel

    override fun setOnClick() {
        super.setOnClick()

        binding.ivBack.setOnSafeClickListener {
            appNavigation.navigateUp()
        }

        binding.tvSubmit.setOnSafeClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            var role = 0
            role = if (binding.radioStudent.isChecked) {
                0
            } else {
                1
            }
            if (isValidAddAccount(name, email, password)) {
                val bundle = Bundle()
                bundle.putString(BundleKey.TITLE_ACTION_SUCCESS, "Add account success")
                bundle.putString(
                    BundleKey.DES_ACTION_SUCCESS,
                    "Congratulation! Add account success. Please continue your work"
                )
                appNavigation.openAddAccountToAddAccountSuccess(bundle)
            }
        }

        binding.edtName.doOnTextChanged { text, start, before, count ->
            binding.tvErrorName.isVisible = false
        }

        binding.edtEmail.doOnTextChanged { text, start, before, count ->
            binding.tvErrorEmail.isVisible = false
        }

        binding.edtPassword.doOnTextChanged { text, start, before, count ->
            binding.tvErrorPassword.isVisible = false
        }
    }

    private fun isValidAddAccount(name: String, email: String, password: String): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            binding.tvErrorName.text = getString(R.string.name_is_empty)
            binding.tvErrorName.isVisible = true
            isValid = false
        }
        if (email.isEmpty()) {
            binding.tvErrorEmail.text = getString(R.string.email_is_empty)
            binding.tvErrorEmail.isVisible = true
            isValid = false
        } else {
            if (!email.isValidEmailInput()) {
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