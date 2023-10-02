package com.example.baseproject.ui.teacher.allAttendance

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAllAttendanceBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.baseproject.util.stickyheadertableview.OnTableCellClickListener
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
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
    }


    private fun initComponents() {
        val stickyHeaderTableView = binding.stickyHeaderTableView
        stickyHeaderTableView.onTableCellClickListener = object : OnTableCellClickListener {
            override fun onTableCellClicked(rowPosition: Int, columnPosition: Int) {
                Toast.makeText(
                    requireContext(),
                    "Row: $rowPosition, Column: $columnPosition",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun getDummyData(
        listSchedule: List<String>,
        listStudent: List<String>
    ): List<List<String>> {
        val row = listStudent.size + 1
        val column = listSchedule.size + 1

        val strings = ArrayList<ArrayList<String>>()
        var innerStrings: ArrayList<String>

        for (i in -1 until row - 1) {
            innerStrings = ArrayList()
            for (j in -1 until column - 1) {
                if (i == -1 && j == -1) {
                    innerStrings.add("Date Student")
                } else if (i == -1) {
                    innerStrings.add(listSchedule[j])
                } else if (j == -1) {
                    innerStrings.add(listStudent[i])
                } else {
                    innerStrings.add((i + 1).toString() + "," + (j + 1).toString())
                }
            }
            strings.add(innerStrings)
        }
        Log.d("asgawgwagwag", "getDummyData: $strings")
        return strings
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.isDone.collectFlowOnView(viewLifecycleOwner) {
                Log.d("asgawgawgwag", "bindingStateView: ${viewModel.allSchedule.value}")
                Log.d("asgawgawgwag", "bindingStateView: ${viewModel.allStudent.value}")
                binding.stickyHeaderTableView.data =
                    getDummyData(viewModel.allSchedule.value, viewModel.allStudent.value)

            }
        }
    }


}