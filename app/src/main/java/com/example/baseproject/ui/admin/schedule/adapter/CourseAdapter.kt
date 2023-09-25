package com.example.baseproject.ui.admin.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCourseBinding
import com.example.baseproject.model.CourseHaveShedule

class CourseAdapter(
) :
    ListAdapter<CourseHaveShedule, CourseAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<CourseHaveShedule>() {
        override fun areItemsTheSame(
            oldItem: CourseHaveShedule,
            newItem: CourseHaveShedule
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CourseHaveShedule,
            newItem: CourseHaveShedule
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseHaveShedule) {
            binding.tvCourseName.text = course.courseName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
