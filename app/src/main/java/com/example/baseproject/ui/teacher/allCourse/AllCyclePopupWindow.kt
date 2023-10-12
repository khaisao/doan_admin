package com.example.baseproject.ui.teacher.allCourse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.databinding.PopUpWindownAllCyclesBinding
import com.example.baseproject.model.OverviewCourseTeacherAssign
import com.example.baseproject.ui.teacher.allCourse.adapter.AllCyclesAdapter

class AllCyclePopupWindow(
    private val context: Context,
    private val onCycleClick: (OverviewCourseTeacherAssign) -> Unit
) {
    private var binding: PopUpWindownAllCyclesBinding
    private var listCycle: List<OverviewCourseTeacherAssign> = emptyList()

    init {
        val inflater = LayoutInflater.from(context)
        binding = PopUpWindownAllCyclesBinding.inflate(inflater)
    }

    fun setData(listCycle: List<OverviewCourseTeacherAssign>) {
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
        val adapter = AllCyclesAdapter {
            onCycleClick.invoke(it)
            popupWindow.dismiss()
        }

        binding.rvAllCycles.layoutManager = LinearLayoutManager(context)
        binding.rvAllCycles.adapter = adapter
        adapter.submitList(listCycle)

        popupWindow.showAsDropDown(anchorView)
    }

}