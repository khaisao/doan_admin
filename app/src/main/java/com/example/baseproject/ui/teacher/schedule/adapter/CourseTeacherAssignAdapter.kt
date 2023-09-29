package com.example.baseproject.ui.teacher.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCourseBinding
import com.example.baseproject.model.CourseTeacherAssign
import com.example.core.utils.setOnSafeClickListener

class CourseTeacherAssignAdapter(
    private var onCourseClick: ((course: CourseTeacherAssign) -> Unit)

) :
    ListAdapter<CourseTeacherAssign, CourseTeacherAssignAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<CourseTeacherAssign>() {
        override fun areItemsTheSame(
            oldItem: CourseTeacherAssign,
            newItem: CourseTeacherAssign
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: CourseTeacherAssign,
            newItem: CourseTeacherAssign
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseTeacherAssign) {
            binding.tvCourseName.text = course.courseName
            binding.root.setOnSafeClickListener {
                onCourseClick.invoke(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseTeacherAssignAdapter.ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
