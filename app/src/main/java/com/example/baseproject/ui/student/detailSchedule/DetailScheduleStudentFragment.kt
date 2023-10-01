package com.example.baseproject.ui.student.detailSchedule

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailScheduleStudentBinding
import com.example.baseproject.databinding.FragmentScheduleStudentBinding
import com.example.baseproject.databinding.FragmentScheduleTeacherBinding
import com.example.baseproject.model.Course
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.student.schedule.adapter.CourseStudentRegisterAdapter
import com.example.baseproject.ui.teacher.schedule.adapter.CourseTeacher
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DetailScheduleStudentFragment :
    BaseFragment<FragmentDetailScheduleStudentBinding, DetailScheduleStudentViewModel>(R.layout.fragment_detail_schedule_student) {
    private val viewModel: DetailScheduleStudentViewModel by viewModels()

    override fun getVM(): DetailScheduleStudentViewModel = viewModel

    private lateinit var adapter: DetailScheduleStudentAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = DetailScheduleStudentAdapter()
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
            viewModel.allDetailScheduleStudent.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)

            }
        }
    }

}