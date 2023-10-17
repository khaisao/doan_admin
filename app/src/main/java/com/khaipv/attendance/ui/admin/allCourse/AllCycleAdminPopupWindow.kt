package com.khaipv.attendance.ui.admin.allCourse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.databinding.PopUpWindownAllCyclesBinding
import com.khaipv.attendance.model.OverViewCourseHaveShedule
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.khaipv.attendance.ui.admin.allCourse.adapter.AllCyclesAdminAdapter
import com.khaipv.attendance.ui.student.allCourse.adapter.AllCyclesStudentAdapter
import com.khaipv.attendance.ui.teacher.allCourse.adapter.AllCyclesTeacherAdapter

class AllCycleAdminPopupWindow(
    private val context: Context,
    private val onCycleClick: (OverViewCourseHaveShedule) -> Unit
) {
    private var binding: PopUpWindownAllCyclesBinding
    private var listCycle: List<OverViewCourseHaveShedule> = emptyList()

    init {
        val inflater = LayoutInflater.from(context)
        binding = PopUpWindownAllCyclesBinding.inflate(inflater)
    }

    fun setData(listCycle: List<OverViewCourseHaveShedule>) {
        this.listCycle = listCycle
    }

    fun showPopup(anchorView: View) {
        // Tạo PopupWindow với kích thước và các thuộc tính tùy chỉnh
        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val adapter = AllCyclesAdminAdapter {
            onCycleClick.invoke(it)
            popupWindow.dismiss()
        }

        binding.rvAllCycles.layoutManager = LinearLayoutManager(context)
        binding.rvAllCycles.adapter = adapter
        adapter.submitList(listCycle)

        popupWindow.showAsDropDown(anchorView)
    }

}