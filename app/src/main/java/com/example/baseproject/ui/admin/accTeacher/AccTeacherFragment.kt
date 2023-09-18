package com.example.baseproject.ui.admin.accTeacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAccTeacherBinding
import com.example.baseproject.databinding.FragmentAdminHomeBinding
import com.example.baseproject.ui.admin.AdminHomeViewModel
import com.example.baseproject.ui.admin.schedule.ScheduleAdminViewModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccTeacherFragment : BaseFragment<FragmentAccTeacherBinding, AccTeacherViewModel>(R.layout.fragment_acc_teacher) {
    private val viewModel: AccTeacherViewModel by viewModels()

    override fun getVM(): AccTeacherViewModel = viewModel

}