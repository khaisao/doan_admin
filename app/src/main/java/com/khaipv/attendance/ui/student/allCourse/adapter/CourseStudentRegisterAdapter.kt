package com.khaipv.attendance.ui.student.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khaipv.attendance.databinding.ItemCourseBinding
import com.khaipv.attendance.model.DetailCourseStudentRegister
import com.example.core.utils.setOnSafeClickListener

class CourseStudentRegisterAdapter(
    private var onCourseClick: ((detailCourseStudentRegister: DetailCourseStudentRegister) -> Unit)
) :
    ListAdapter<DetailCourseStudentRegister, CourseStudentRegisterAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DetailCourseStudentRegister>() {
        override fun areItemsTheSame(
            oldItem: DetailCourseStudentRegister,
            newItem: DetailCourseStudentRegister
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DetailCourseStudentRegister,
            newItem: DetailCourseStudentRegister
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: DetailCourseStudentRegister) {
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
