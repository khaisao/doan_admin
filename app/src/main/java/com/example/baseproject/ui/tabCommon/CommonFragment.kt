package com.example.baseproject.ui.tabCommon

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentCommonBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonFragment : BaseFragment<FragmentCommonBinding, CommonViewModel>() {

    private val viewModel: CommonViewModel by viewModels()

    override val layoutId = R.layout.fragment_common

    override fun getVM(): CommonViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

}