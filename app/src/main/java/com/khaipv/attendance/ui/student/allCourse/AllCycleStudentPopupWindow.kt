package com.khaipv.attendance.ui.student.allCourse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.databinding.PopUpWindownAllCyclesBinding
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.khaipv.attendance.ui.student.allCourse.adapter.AllCyclesStudentAdapter
import com.khaipv.attendance.ui.teacher.allCourse.adapter.AllCyclesTeacherAdapter

class AllCycleStudentPopupWindow(
    private val context: Context,
    private val onCycleClick: (OverviewCourseStudentRegister) -> Unit
) {
    private var binding: PopUpWindownAllCyclesBinding
    private var listCycle: List<OverviewCourseStudentRegister> = emptyList()

    init {
        val inflater = LayoutInflater.from(context)
        binding = PopUpWindownAllCyclesBinding.inflate(inflater)
    }

    fun setData(listCycle: List<OverviewCourseStudentRegister>) {
        this.listCycle = listCycle
    }

    fun showPopup(anchorView: View) {
        if (listCycle.isNotEmpty()) {
            val popupWindow = PopupWindow(
                binding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            val adapter = AllCyclesStudentAdapter {
                onCycleClick.invoke(it)
                popupWindow.dismiss()
            }

            binding.rvAllCycles.layoutManager = LinearLayoutManager(context)
            binding.rvAllCycles.adapter = adapter
            adapter.submitList(listCycle)

            popupWindow.showAsDropDown(anchorView)
        }
    }
}