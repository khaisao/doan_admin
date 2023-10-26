package com.khaipv.attendance.ui.teacher.editSchedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.databinding.PopUpWindownAllCyclesBinding
import com.khaipv.attendance.model.Classroom
import com.khaipv.attendance.model.OverviewCourseTeacherAssign
import com.khaipv.attendance.ui.teacher.allCourse.adapter.AllCyclesTeacherAdapter
import com.khaipv.attendance.ui.teacher.editSchedule.adapter.AllClassroomTeacherAdapter

class AllClassroomTeacherPopupWindow(
    private val context: Context,
    private val onClassroomClick: (Classroom) -> Unit
) {
    private var binding: PopUpWindownAllCyclesBinding
    private var listCycle: List<Classroom> = emptyList()

    init {
        val inflater = LayoutInflater.from(context)
        binding = PopUpWindownAllCyclesBinding.inflate(inflater)
    }

    fun setData(listClassroom: List<Classroom>) {
        this.listCycle = listClassroom
    }

    fun showPopup(anchorView: View) {
        // Tạo PopupWindow với kích thước và các thuộc tính tùy chỉnh
        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val adapter = AllClassroomTeacherAdapter {
            onClassroomClick.invoke(it)
            popupWindow.dismiss()
        }

        binding.rvAllCycles.layoutManager = LinearLayoutManager(context)
        binding.rvAllCycles.adapter = adapter
        adapter.submitList(listCycle)

        popupWindow.showAsDropDown(anchorView)
    }

}