package com.example.baseproject.ui.student.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCourseBinding
import com.example.baseproject.model.Course
import com.example.baseproject.model.CourseStudentRegister
import com.example.core.utils.setOnSafeClickListener

class CourseStudentRegisterAdapter(
    private var onCourseClick: ((courseStudentRegister: CourseStudentRegister) -> Unit)
) :
    ListAdapter<CourseStudentRegister, CourseStudentRegisterAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<CourseStudentRegister>() {
        override fun areItemsTheSame(
            oldItem: CourseStudentRegister,
            newItem: CourseStudentRegister
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: CourseStudentRegister,
            newItem: CourseStudentRegister
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseStudentRegister) {
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

    override fun onBindViewHolder(holder: CourseStudentRegisterAdapter.ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
