package com.example.baseproject.ui.student.detailAttendance

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailScheduleStudentBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailAttendanceStudentFragment :
    BaseFragment<FragmentDetailScheduleStudentBinding, DetailAttendanceStudentViewModel>(R.layout.fragment_detail_schedule_student) {
    private val viewModel: DetailAttendanceStudentViewModel by viewModels()

    override fun getVM(): DetailAttendanceStudentViewModel = viewModel

    private lateinit var adapter: DetailAttendanceStudentAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = DetailAttendanceStudentAdapter()
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        val coursePerCyclesId = arguments?.getInt(BundleKey.COURSE_PER_CYCLE_ID)
        if (coursePerCyclesId != null) {
            viewModel.getDetailScheduleStudent(coursePerCyclesId)
        }

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allDetailAttendanceStudent.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)

            }
        }
    }

}