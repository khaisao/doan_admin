package com.example.baseproject.ui.teacher.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCourseBinding
import com.example.baseproject.model.DetailCourseTeacherAssign
import com.example.core.utils.setOnSafeClickListener
import java.util.Locale

class CourseTeacherAssignAdapter(
    private var onCourseClick: ((course: DetailCourseTeacherAssign) -> Unit)
) :
    ListAdapter<DetailCourseTeacherAssign, CourseTeacherAssignAdapter.ConsultantHolder>(DiffCallback()) {

    private var listCurrentList: List<DetailCourseTeacherAssign> = emptyList()

//    override fun submitList(list: MutableList<DetailCourseTeacherAssign>?) {
//        super.submitList(list)
//        if (list != null) {
//            listCurrentList = list
//        }
//    }

    fun filterList(query: String) {
        val filteredList = if (query.isBlank()) {
            listCurrentList // Không có từ khóa tìm kiếm, trả về danh sách gốc
        } else {
            listCurrentList.filter { course ->
                // Thực hiện tìm kiếm dựa trên từ khóa trong đây
                course.courseName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }
        }
        submitList(filteredList.toMutableList()) // Cập nhật danh sách hiển thị trong RecyclerView
    }

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
