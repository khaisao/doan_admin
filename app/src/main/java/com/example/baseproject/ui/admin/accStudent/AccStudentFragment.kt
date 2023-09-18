package com.example.baseproject.ui.admin.accStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAccStudentBinding
import com.example.baseproject.databinding.FragmentAdminHomeBinding
import com.example.baseproject.ui.admin.AdminHomeViewModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccStudentFragment :
    BaseFragment<FragmentAccStudentBinding, AccStudentViewModel>(R.layout.fragment_acc_student) {
    private val viewModel: AccStudentViewModel by viewModels()

    override fun getVM(): AccStudentViewModel = viewModel

}