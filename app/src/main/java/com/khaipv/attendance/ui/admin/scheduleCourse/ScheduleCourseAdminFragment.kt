package com.khaipv.attendance.ui.admin.scheduleCourse

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentDetailCourseBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleCourseAdminFragment :
    BaseFragment<FragmentDetailCourseBinding, ScheduleCourseAdminViewModel>(R.layout.fragment_detail_course) {

    private val viewModel: ScheduleCourseAdminViewModel by viewModels()

    override fun getVM(): ScheduleCourseAdminViewModel = viewModel

    private lateinit var adapter: ScheduleCourseAdminAdapter

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = ScheduleCourseAdminAdapter(onCourseClick = {}, onSeeAttendance = {})
        binding.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter

        val courseId = arguments?.getInt(BundleKey.COURSE_ID_TO_GET_SCHEDULE)
        if (courseId != null) {
            viewModel.getAllScheduleSpecificCourse(courseId)
        } else {
            toastMessage("Error, try again")
            appNavigation.navigateUp()
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.allSchedule.collectFlowOnView(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}