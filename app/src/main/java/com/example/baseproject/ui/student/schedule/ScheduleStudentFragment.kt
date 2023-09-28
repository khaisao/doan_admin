package com.example.baseproject.ui.student.schedule

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
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
class ScheduleStudentFragment :
    BaseFragment<FragmentScheduleStudentBinding, ScheduleStudentViewModel>(R.layout.fragment_schedule_student) {
    private val viewModel: ScheduleStudentViewModel by viewModels()

    override fun getVM(): ScheduleStudentViewModel = viewModel

    private lateinit var adapter: CourseStudentRegisterAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseStudentRegisterAdapter(onCourseClick = {
            val bundle = Bundle()
            bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID, 1)
//            appNavigation.openScheduleToDetailCourse(bundle)
        })
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        viewModel.getAllCourseRegister()

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                adapter.submitList(viewModel.allCourseStudentRegister.value)
            } else {
                val filterList =
                    viewModel.allCourseStudentRegister.value.filter {
                        it.courseName.lowercase(Locale.getDefault())
                            .contains(text.toString().lowercase(Locale.ROOT))
                    }
                adapter.submitList(filterList)
            }

        }

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allCourseStudentRegister.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)

            }
        }
    }

}