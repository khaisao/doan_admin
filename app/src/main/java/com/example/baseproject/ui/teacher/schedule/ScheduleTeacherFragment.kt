package com.example.baseproject.ui.teacher.schedule

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleTeacherBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.teacher.schedule.adapter.CourseTeacherAssignAdapter
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleTeacherFragment :
    BaseFragment<FragmentScheduleTeacherBinding, ScheduleTeacherViewModel>(R.layout.fragment_schedule_teacher) {
    private val viewModel: ScheduleTeacherViewModel by viewModels()

    override fun getVM(): ScheduleTeacherViewModel = viewModel

    private lateinit var adapter: CourseTeacherAssignAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseTeacherAssignAdapter(onCourseClick = {
            val bundle = Bundle()
            toastMessage(it.coursePerCycleId.toString())
            bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID, it.coursePerCycleId)
            appNavigation.openScheduleToDetailCourse(bundle)
        })
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        binding.ivAvatar.loadImage(rxPreferences.getAvatar())

        viewModel.getAllCourseAssign()

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                adapter.submitList(viewModel.allCourseTeacherAssign.value)
            } else {
                val filterList =
                    viewModel.allCourseTeacherAssign.value.filter {
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
            viewModel.allCourseTeacherAssign.collectFlowOnView(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }

}