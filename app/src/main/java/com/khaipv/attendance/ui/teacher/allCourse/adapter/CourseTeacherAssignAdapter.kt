package com.khaipv.attendance.ui.teacher.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.pref.RxPreferences
import com.example.core.utils.loadImage
import com.khaipv.attendance.databinding.ItemCourseBinding
import com.khaipv.attendance.model.DetailCourseTeacherAssign
import com.example.core.utils.setOnSafeClickListener
import com.khaipv.attendance.R
import java.util.Locale

class CourseTeacherAssignAdapter(
    private var onCourseClick: ((course: DetailCourseTeacherAssign) -> Unit),
    private val rxPreferences: RxPreferences
) :
    ListAdapter<DetailCourseTeacherAssign, CourseTeacherAssignAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DetailCourseTeacherAssign>() {
        override fun areItemsTheSame(
            oldItem: DetailCourseTeacherAssign,
            newItem: DetailCourseTeacherAssign
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DetailCourseTeacherAssign,
            newItem: DetailCourseTeacherAssign
        ): Boolean {
            return false
        }
    }

    inner class ConsultantHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: DetailCourseTeacherAssign) {
            binding.tvCourseName.text = course.courseName
            binding.root.setOnSafeClickListener {
                onCourseClick.invoke(course)
            }
            binding.ivAvatar.loadImage(rxPreferences.getAvatar(), R.drawable.no_avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CourseTeacherAssignAdapter.ConsultantHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}
