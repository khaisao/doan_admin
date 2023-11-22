package com.khaipv.attendance.ui.student.detailAttendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentDetailScheduleStudentBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
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
        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCourse.adapter = adapter

        val coursePerCyclesId = arguments?.getInt(BundleKey.COURSE_PER_CYCLE_ID)
        Log.d("asgagwwagwagwga", "initView: $coursePerCyclesId")
        if (coursePerCyclesId != null) {
            viewModel.getDetailScheduleStudent(coursePerCyclesId)
        }

        binding.ivAvatar.loadImage(rxPreferences.getAvatar(), R.drawable.no_avatar)

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