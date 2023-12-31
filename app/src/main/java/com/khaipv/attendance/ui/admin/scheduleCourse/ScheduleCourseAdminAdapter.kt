package com.khaipv.attendance.ui.admin.scheduleCourse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khaipv.attendance.databinding.ItemScheduleBinding
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.util.DateFormat
import com.example.core.utils.setOnSafeClickListener
import com.khaipv.attendance.databinding.ItemScheduleAdminBinding
import com.khaipv.attendance.util.toDateWithFormatInputAndOutPut

class ScheduleCourseAdminAdapter(
    private var onCourseClick: ((schedule: DetailScheduleCourse) -> Unit),
    private var onSeeAttendance: ((schedule: DetailScheduleCourse) -> Unit),
) :
    ListAdapter<DetailScheduleCourse, ScheduleCourseAdminAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DetailScheduleCourse>() {
        override fun areItemsTheSame(
            oldItem: DetailScheduleCourse,
            newItem: DetailScheduleCourse
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DetailScheduleCourse,
            newItem: DetailScheduleCourse
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemScheduleAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: DetailScheduleCourse) {
            binding.tvCourseName.text = schedule.courseName
            binding.tvClassroomName.text = schedule.classroomName
            binding.tvTime.text = schedule.startTime.toDateWithFormatInputAndOutPut(DateFormat.FORMAT_1, DateFormat.FORMAT_4) + " - " + schedule.endTime.toDateWithFormatInputAndOutPut(
                DateFormat.FORMAT_1, DateFormat.FORMAT_4)
            binding.root.setOnClickListener {
                binding.btnAttendance.isVisible = binding.btnAttendance.isVisible != true
                binding.btnSeeAttendance.isVisible = binding.btnSeeAttendance.isVisible != true
            }
            binding.btnAttendance.setOnSafeClickListener {
                onCourseClick.invoke(schedule)
            }
            binding.btnSeeAttendance.setOnSafeClickListener {
                onSeeAttendance.invoke(schedule)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduleAdminBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
