package com.example.baseproject.ui.admin.schedule

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleAdminBinding
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleAdminFragment :
    BaseFragment<FragmentScheduleAdminBinding, ScheduleAdminViewModel>(R.layout.fragment_schedule_admin) {
    private val viewModel: ScheduleAdminViewModel by viewModels()

    override fun getVM(): ScheduleAdminViewModel = viewModel

}