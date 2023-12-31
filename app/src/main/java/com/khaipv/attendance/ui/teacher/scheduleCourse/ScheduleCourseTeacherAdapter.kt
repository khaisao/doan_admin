package com.khaipv.attendance.ui.teacher.scheduleCourse

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
import com.khaipv.attendance.util.toDateWithFormatInputAndOutPut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleCourseTeacherAdapter(
    private var onAttendanceClick: ((schedule: DetailScheduleCourse) -> Unit),
    private var onSeeAttendance: ((schedule: DetailScheduleCourse) -> Unit),
    private var onEditSchedule: ((schedule: DetailScheduleCourse) -> Unit),
    private val viewModel: ScheduleCourseTeacherViewModel
) :
    ListAdapter<DetailScheduleCourse, ScheduleCourseTeacherAdapter.ConsultantHolder>(DiffCallback()) {

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

    inner class ConsultantHolder(val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: DetailScheduleCourse) {
            binding.tvCourseName.text = schedule.courseName
            binding.tvClassroomName.text = schedule.classroomName
            binding.tvTime.text = buildString {
                append(
                    schedule.startTime.toDateWithFormatInputAndOutPut(
                        DateFormat.FORMAT_1,
                        DateFormat.FORMAT_5
                    )
                )
                append(" - ")
                append(
                    schedule.endTime.toDateWithFormatInputAndOutPut(
                        DateFormat.FORMAT_1, DateFormat.FORMAT_5
                    )
                )
            }
            binding.tvLessonOrder.text = "Lesson ${adapterPosition + 1}: "
            binding.root.setOnClickListener {
                binding.btnAttendance.isVisible = binding.btnAttendance.isVisible != true
                binding.btnSeeAttendance.isVisible = binding.btnSeeAttendance.isVisible != true
                binding.btnEdit.isVisible = binding.btnEdit.isVisible != true
            }
            binding.btnAttendance.setOnSafeClickListener {
                onAttendanceClick.invoke(schedule)
            }
            binding.btnSeeAttendance.setOnSafeClickListener {
                onSeeAttendance.invoke(schedule)
            }
            binding.btnEdit.setOnSafeClickListener {
                onEditSchedule.invoke(schedule)
            }
            CoroutineScope(Dispatchers.IO).launch {
                val listAttendance = viewModel.getAllAttendanceSpecificSchedule(
                    schedule.coursePerCycleId,
                    schedule.scheduleId
                )
                var isAlreadyAttendance = false
                for(item in listAttendance){
                    if(!item.timeAttendance.isNullOrBlank()){
                        isAlreadyAttendance = true
                    }
                }
                //Todo remove comment
//                withContext(Dispatchers.Main){
//                    if(!isAlreadyAttendance){
//                        binding.btnAttendance.isEnabled = true
//                        binding.btnAttendance.setBackgroundResource(R.drawable.bg_btn_log_out)
//                    } else {
//                        binding.btnAttendance.isEnabled = false
//                        binding.btnAttendance.setBackgroundResource(R.drawable.bg_btn_primary_disable)
//                    }
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduleBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleCourseTeacherAdapter.ConsultantHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}
