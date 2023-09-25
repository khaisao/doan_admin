package com.example.baseproject.ui.teacher.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCourseBinding
import com.example.baseproject.model.Course
import com.example.core.utils.setOnSafeClickListener

class CourseTeacher(
    private var onCourseClick: ((course: Course) -> Unit)

) :
    ListAdapter<Course, CourseTeacher.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.tvCourseName.text = course.name
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

    override fun onBindViewHolder(holder: CourseTeacher.ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
