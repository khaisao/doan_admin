package com.example.baseproject.ui.login

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()

    override fun getVM(): LoginViewModel = viewModel

}