package com.example.baseproject.ui.teacher.detailAttendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemAttendanceOfStudentBinding
import com.example.baseproject.model.DetailAttendanceStudentTeacherScreen
import com.example.baseproject.util.DateFormat
import com.example.baseproject.util.DateFormat.Companion.FORMAT_1
import com.example.baseproject.util.toDateWithFormatInputAndOutPut
import com.example.core.utils.loadImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailAttendanceTeacherAdapter :
    ListAdapter<DetailAttendanceStudentTeacherScreen, DetailAttendanceTeacherAdapter.ConsultantHolder>(
        DiffCallback()
    ) {

    class DiffCallback : DiffUtil.ItemCallback<DetailAttendanceStudentTeacherScreen>() {
        override fun areItemsTheSame(
            oldItem: DetailAttendanceStudentTeacherScreen,
            newItem: DetailAttendanceStudentTeacherScreen
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DetailAttendanceStudentTeacherScreen,
            newItem: DetailAttendanceStudentTeacherScreen
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemAttendanceOfStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailAttendanceStudentTeacherScreen) {
            binding.tvStudentId.text = item.studentId.toString()
            binding.tvStudentName.text = item.studentName
            binding.tvStudentAttendanceTime.text =
                item.timeAttendance?.toDateWithFormatInputAndOutPut(
                    DateFormat.FORMAT_1,
                    DateFormat.FORMAT_4
                ) ?: ""
            if (item.startTime != null && isScheduleIsPass(item.startTime!!)) {
                if (item.timeAttendance != null) {
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
        val binding = ItemAttendanceOfStudentBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailAttendanceTeacherAdapter.ConsultantHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}
