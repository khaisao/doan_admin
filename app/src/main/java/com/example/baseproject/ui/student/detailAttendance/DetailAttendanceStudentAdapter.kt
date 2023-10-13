package com.example.baseproject.ui.student.detailAttendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemScheduleWithAttendanceContentBinding
import com.example.baseproject.model.DetailAttendanceStudent
import com.example.baseproject.util.DateFormat.Companion.FORMAT_1
import com.example.baseproject.util.DateFormat.Companion.FORMAT_2
import com.example.baseproject.util.DateFormat.Companion.FORMAT_3
import com.example.core.utils.loadImage
import com.example.baseproject.util.toDateWithFormatInputAndOutPut
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailAttendanceStudentAdapter :
    ListAdapter<DetailAttendanceStudent, DetailAttendanceStudentAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DetailAttendanceStudent>() {
        override fun areItemsTheSame(
            oldItem: DetailAttendanceStudent,
            newItem: DetailAttendanceStudent
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DetailAttendanceStudent,
            newItem: DetailAttendanceStudent
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemScheduleWithAttendanceContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: DetailAttendanceStudent) {
            binding.tvDate.text =
                course.startTime.toDateWithFormatInputAndOutPut(FORMAT_1, FORMAT_2)
            binding.tvHour.text = course.startTime.toDateWithFormatInputAndOutPut(
                FORMAT_1,
                FORMAT_3
            ) + "\n" + course.endTime.toDateWithFormatInputAndOutPut(FORMAT_1, FORMAT_3)
            binding.tvClassroomName.text = course.classroomName
            if (isScheduleIsPass(course.startTime)) {
                if (course.timeAttendance != null) {
                    binding.ivIsAttendace.loadImage(R.drawable.ic_check)
                } else {
                    binding.ivIsAttendace.loadImage(R.drawable.ic_uncheck)
                }
            } else {
                binding.ivIsAttendace.setImageDrawable(null)
            }
        }
    }

    private fun isScheduleIsPass(timeSchedule: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat(FORMAT_1, Locale.US)
            val date = dateFormat.parse(timeSchedule)
            val currentTime = Date()
            !date?.after(currentTime)!!
        } catch (e: Exception) {
            false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduleWithAttendanceContentBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailAttendanceStudentAdapter.ConsultantHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}
