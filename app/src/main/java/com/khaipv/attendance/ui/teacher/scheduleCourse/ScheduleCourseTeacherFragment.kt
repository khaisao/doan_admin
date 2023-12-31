package com.khaipv.attendance.ui.teacher.scheduleCourse

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentDetailCourseBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.toastMessage
import com.khaipv.attendance.shareData.ShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleCourseTeacherFragment :
    BaseFragment<FragmentDetailCourseBinding, ScheduleCourseTeacherViewModel>(R.layout.fragment_detail_course) {

    private val viewModel: ScheduleCourseTeacherViewModel by viewModels()

    private val shareViewModel: ShareViewModel by activityViewModels()

    override fun getVM(): ScheduleCourseTeacherViewModel = viewModel

    private lateinit var adapter: ScheduleCourseTeacherAdapter

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        shareViewModel.clearData()
        adapter = ScheduleCourseTeacherAdapter(
            onAttendanceClick = {
                val bundle = Bundle()
                bundle.putInt(BundleKey.COURSE_ID_ATTENDANCE, it.coursePerCycleId)
                bundle.putInt(BundleKey.SCHEDULE_ID_ATTENDANCE, it.scheduleId)
//                appNavigation.openScheduleCourseToFaceRecoFaceNet(bundle)
                appNavigation.openScheduleCourseToFaceRecoKbi(bundle)
            },
            onSeeAttendance = {
                val bundle = Bundle()
                bundle.putInt(BundleKey.COURSE_ID_ATTENDANCE, it.coursePerCycleId)
                bundle.putInt(BundleKey.SCHEDULE_ID_ATTENDANCE, it.scheduleId)
                bundle.putParcelable(BundleKey.ITEM_SCHEDULE, it)
                appNavigation.openScheduleCourseToHistoryAttendance(bundle)
            },
            onEditSchedule = {
                val bundle = Bundle()
                bundle.putParcelable(BundleKey.ITEM_SCHEDULE_TO_EDIT, it)
                appNavigation.openScheduleTeacherToEditSchedule(bundle)
            },
            viewModel
        )
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

    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.allSchedule.collectFlowOnView(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}