package com.khaipv.attendance.ui.admin.addAccount

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentAddAccountBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
            val userName = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            val role: Int = if (binding.radioStudent.isChecked) {
                1
            } else {
                2
            }
            if (isValidAddAccount(name, userName, password)) {
                viewModel.registerAccount(
                    userName = userName,
                    password = password,
                    name = name,
                    role = role
                )
            }

        }

        binding.edtName.doOnTextChanged { text, start, before, count ->
            binding.tvErrorName.isVisible = false
        }

        binding.edtUsername.doOnTextChanged { text, start, before, count ->
            binding.tvErrorUsername.isVisible = false
        }

        binding.edtPassword.doOnTextChanged { text, start, before, count ->
            binding.tvErrorPassword.isVisible = false
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.addAccountActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is AddAccountEvent.AddAccountSuccess) {
                    val bundle = Bundle()
                    bundle.putString(BundleKey.TITLE_ACTION_SUCCESS, "Add account success")
                    bundle.putString(
                        BundleKey.DES_ACTION_SUCCESS,
                        "Congratulation! Add account success. Please continue your work"
                    )
                    appNavigation.openAddAccountToAddAccountSuccess(bundle)
                }
            }
        }
    }

    private fun isValidAddAccount(name: String, userName: String, password: String): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            binding.tvErrorName.text = getString(R.string.name_is_empty)
            binding.tvErrorName.isVisible = true
            isValid = false
        }
        if (userName.isEmpty()) {
            binding.tvErrorUsername.text = getString(R.string.username_is_empty)
            binding.tvErrorUsername.isVisible = true
            isValid = false
        } else {
            if (userName.length < 6) {
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