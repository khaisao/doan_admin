package com.example.baseproject.ui.admin.schedule

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleAdminBinding
import com.example.baseproject.model.Course
import com.example.baseproject.ui.admin.schedule.adapter.CourseAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleAdminFragment :
    BaseFragment<FragmentScheduleAdminBinding, ScheduleAdminViewModel>(R.layout.fragment_schedule_admin) {
    private val viewModel: ScheduleAdminViewModel by viewModels()

    override fun getVM(): ScheduleAdminViewModel = viewModel

    private lateinit var adapter: CourseAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseAdapter()
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        viewModel.getAllCourseHaveSchedule()

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.listCourseHaveShedule.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

}