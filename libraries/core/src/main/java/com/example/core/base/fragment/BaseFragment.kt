package com.example.core.base.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.example.core.base.BaseViewModel
import com.example.core.utils.toastMessage

abstract class BaseFragment<BD : ViewDataBinding, VM : BaseViewModel>(@LayoutRes id: Int) :
    BaseFragmentNotRequireViewModel<BD>(id) {

    private lateinit var viewModel: VM

    abstract fun getVM(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    override fun initView(savedInstanceState: Bundle?) {

        viewModel.messageError.observe(viewLifecycleOwner) {
            toastMessage(it.toString())
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showHideLoading(it)
        }

    }

}