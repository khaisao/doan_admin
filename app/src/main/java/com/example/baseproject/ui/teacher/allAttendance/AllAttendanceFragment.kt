package com.example.baseproject.ui.teacher.allAttendance

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAllAttendanceBinding
import com.example.baseproject.model.DetailAttendanceStudent
import com.example.baseproject.model.OverviewScheduleStudent
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.baseproject.util.stickyheadertableview.OnTableCellClickListener
import com.example.core.base.fragment.BaseFragment
import com.example.baseproject.util.DateFormat
import com.example.core.utils.collectFlowOnView
import com.example.baseproject.util.toDateWithFormatInputAndOutPut
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllAttendanceFragment :
    BaseFragment<FragmentAllAttendanceBinding, AllAttendanceViewModel>(R.layout.fragment_all_attendance) {

    private val viewModel: AllAttendanceViewModel by viewModels()

    override fun getVM(): AllAttendanceViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val courseId = arguments?.getInt(BundleKey.COURSE_PER_CYCLE_ID_ALL_ATTENDANCE)
        if (courseId != null) {
            viewModel.getAllAttendanceSpecificCourse(courseId)
        } else {
            toastMessage("Error, try again")
        }
        initComponents()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun initComponents() {
        val stickyHeaderTableView = binding.stickyHeaderTableView
        stickyHeaderTableView.onTableCellClickListener = object : OnTableCellClickListener {
            override fun onTableCellClicked(rowPosition: Int, columnPosition: Int) {
            }
        }


    }

    private fun getDummyData(
        allAttendance: List<OverviewScheduleStudent>
    ): List<List<String>> {
        val strings = ArrayList<ArrayList<String>>()

        try {
            val row = allAttendance.size + 1
            val column = allAttendance[0].schedules.size + 1

            val map = mutableMapOf<Int, List<DetailAttendanceStudent>>()

            for (item in allAttendance) {
                map[item.studentId] = item.schedules
            }

            var innerStrings: ArrayList<String>
            if (row > 0 && column > 0) {
                for (i in -1 until row - 1) {
                    innerStrings = ArrayList()
                    for (j in -1 until column - 1) {
                        if (i == -1 && j == -1) {
                            innerStrings.add("Date Student")
                        } else if (i == -1) {
                            innerStrings.add(
                                allAttendance[0].schedules[j].startTime.toDateWithFormatInputAndOutPut(
                                    DateFormat.FORMAT_1,
                                    DateFormat.FORMAT_2
                                )
                            )
                        } else if (j == -1) {
                            innerStrings.add(allAttendance[i].studentName)
                        } else {
                            val studentId = allAttendance[i].studentId
                            val scheduleId = allAttendance[i].schedules[j].scheduleId
                            val listSchedule = map[studentId]
                            val timeAttendance =
                                listSchedule?.find { it.scheduleId == scheduleId }?.timeAttendance
                            if (timeAttendance != null) {
                                innerStrings.add("v")
                            } else {
                                innerStrings.add("x")
                            }
                        }
                    }
                    strings.add(innerStrings)
                }
            }
            return strings
        } catch (e: Exception) {
            return strings
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allAttendance.collectFlowOnView(viewLifecycleOwner) {
                if (viewModel.allAttendance.value.isNotEmpty()) {
                    if (viewModel.allAttendance.value[0].schedules.isNotEmpty()) {
                        binding.stickyHeaderTableView.data =
                            getDummyData(
                                viewModel.allAttendance.value
                            )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroyView()
    }

}