package com.example.baseproject.ui.teacher.scheduleCourse

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailCourseBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.teacher.allCourse.adapter.CourseTeacherAssignAdapter
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleCourseFragment :
    BaseFragment<FragmentDetailCourseBinding, ScheduleCourseViewModel>(R.layout.fragment_detail_course) {

    private val viewModel: ScheduleCourseViewModel by viewModels()

    override fun getVM(): ScheduleCourseViewModel = viewModel

    private lateinit var adapter: ScheduleCourseAdapter

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = ScheduleCourseAdapter(onCourseClick = {
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.isLoading.postValue(true)
                val bundle = Bundle()
                bundle.putInt(BundleKey.COURSE_ID_ATTENDANCE, it.coursePerCycleId)
                bundle.putInt(BundleKey.SCHEDULE_ID_ATTENDANCE, it.scheduleId)
                appNavigation.openDetailCourseToFaceReco(bundle)
            }
        })
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

    override fun setOnClick() {
        super.setOnClick()
        binding.tvSeeAllAttendance.setOnSafeClickListener {
            val courseId = arguments?.getInt(BundleKey.COURSE_PER_CYCLE_ID)
            if (courseId != null) {
                val bundle = Bundle()
                bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID_ALL_ATTENDANCE, courseId)
                appNavigation.openDetailCourseToAllAttendance(bundle)
            } else {
                toastMessage("Error, try again")
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.allSchedule.collectFlowOnView(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}